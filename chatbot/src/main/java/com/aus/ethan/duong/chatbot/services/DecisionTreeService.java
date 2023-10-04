package com.aus.ethan.duong.chatbot.services;

import com.aus.ethan.duong.chatbot.models.ChatState;
import com.aus.ethan.duong.chatbot.models.Question;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DecisionTreeService {
    private final ChatStateService chatStateService;
    private Question startNode;
    private Question errorNode;
    private Map<String, Question> decisionTreeNodes;
    private Map<String, Map<String, Question>> decisionTree;
    public final String questionDoesNotExist = "Oops, something went wrong. The current question does not exist.";
    public final String restartDialogue = "You reached the end of the conversation so I will begin from the start... ";
    public final String duplicateNames = "Duplicate question names received";

    /**
     * Construct DecisionTreeService
     * @param questionService {QuestionService}
     * @param chatStateService {ChatStateService}
     */
    public DecisionTreeService(QuestionService questionService, ChatStateService chatStateService) {
        this.chatStateService = chatStateService;
        try {
            List<Question> allQuestions = questionService.getAllQuestions();
            if (allQuestions.size() < 2) {
                throw new RuntimeException("Error, database is not populated or start/error nodes are missing");
            }
            this.startNode = allQuestions.get(1);
            this.errorNode = allQuestions.get(0);
            this.decisionTreeNodes = this.createNodeMap(allQuestions);
            this.decisionTree = this.createDecisionTree(allQuestions);
        } catch (Exception e) {
            this.startNode = new Question(1L, "START", null, null, "START");
            this.errorNode = new Question(0L, "ERROR", null, null, "ERROR");
            this.decisionTreeNodes = new HashMap<>();
            this.decisionTree = new HashMap<>();

            // Simulate logging the error
            System.out.println(e.toString());
        }
    }

    /**
     * Reset user state to start node
     * @param username {String}
     * @return startNode text {String}
     */
    public String reset(String username) {
        ChatState startingState = ChatState
                .builder()
                .state(startNode.getName())
                .username(username)
                .build();
        chatStateService.saveChatState(startingState);
        return startNode.getText();
    }

    /**
     * Get question text from user's current question
     * @param username {String}
     * @return current question text {String}
     */
    public String getCurrentQuestion(String username) {
        Optional<ChatState> optionalChatState = chatStateService.getChatState(username);
        if (optionalChatState.isEmpty()) {
            return this.reset(username);
        }
        String currentStateName = optionalChatState.get().getState();
        if (!decisionTreeNodes.containsKey(currentStateName)) {
            this.reset(username);
            return questionDoesNotExist;
        }
        return decisionTreeNodes.get(currentStateName).getText();
    }

    /**
     * Take user response and grab the next question if it exists
     * @param username {String}
     * @param message {String}
     * @return {String}
     */
    public String getResponse(String username, String message) {
        message = message.toLowerCase();
        Optional<ChatState> optionalChatState = chatStateService.getChatState(username);

        // Case: No stored chat state for user
        if (optionalChatState.isEmpty()) {
            return this.reset(username);
        }
        String currentStateName = optionalChatState.get().getState();

        // Case: Question/state name does not exist as node in decision tree
        if (!decisionTreeNodes.containsKey(currentStateName) || !decisionTree.containsKey(currentStateName)) {
            this.reset(username);
            return questionDoesNotExist;
        }
        Map<String,Question> currentFollowupQuestions = decisionTree.get(currentStateName);

        // Case: The node is a terminal node
        if(currentFollowupQuestions != null && currentFollowupQuestions.isEmpty()) {
            this.reset(username);
            return restartDialogue + startNode.getText();
        }

        // Case: The user response is not recognized as a valid response
        if (currentFollowupQuestions == null || !currentFollowupQuestions.containsKey(message)) {
            String currentQuestionText = decisionTreeNodes.get(currentStateName).getText();
            String errorQuestionText = errorNode.getText();
            return errorQuestionText + " " + currentQuestionText;
        }

        // Case: The user response is valid. Therefore, move user to the next state and return next question
        Question nextQuestion = currentFollowupQuestions.get(message);
        ChatState nextChatState = ChatState
                .builder()
                .username(username)
                .state(nextQuestion.getName())
                .build();
        chatStateService.saveChatState(nextChatState);

        return nextQuestion.getText();
    }

    /**
     * Create map of question names to Questions
     * @param allQuestions {List: Question}
     * @return {Map: String,Question}
     */
    public Map<String, Question> createNodeMap(List<Question> allQuestions) {
        Map<String, Question> questionNodes = new HashMap<>();

        for (Question question : allQuestions) {
            if (questionNodes.containsKey(question.getName())) {
                throw new RuntimeException(duplicateNames);
            }
            questionNodes.put(question.getName(), question);
        }
        return questionNodes;
    }

    /**
     * Generate decision tree by mapping parent questions to followup questions
     * @param allQuestions {List: Question}
     * @return {Map (String, Map(String, Question))}
     */
    public Map<String, Map<String, Question>> createDecisionTree(List<Question> allQuestions) {
        Map<String, Map<String, Question>> followUpQuestions = new HashMap<>();
        for (Question question : allQuestions) {
            if (followUpQuestions.containsKey(question.getName())) {
                throw new RuntimeException(duplicateNames);
            }
            followUpQuestions.put(question.getName(), new HashMap<>());
        }
        for (Question question : allQuestions) {
            if(question.getParent() != null && question.getPrevious_decision() != null) {
                followUpQuestions.get(question.getParent())
                        .put(question.getPrevious_decision().toLowerCase(), question);
            }
        }
        return followUpQuestions;
    }
}
