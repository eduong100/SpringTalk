package com.aus.ethan.duong.chatbot.services;

import com.aus.ethan.duong.chatbot.models.ChatState;
import com.aus.ethan.duong.chatbot.models.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
public class DecisionTreeServiceTest {
    @Mock
    private ChatStateService chatStateService;
    @Mock
    private QuestionService questionService;

    @InjectMocks
    private DecisionTreeService decisionTreeService;
    private ChatState invalidChatState;
    private ChatState validChatState;
    private List<Question> exampleQuestions;
    private List<Question> exampleQuestionsWithDuplicateNames;
    @BeforeEach
    public void setup() {
        invalidChatState = ChatState
                .builder()
                .id(1L)
                .username("Ethan")
                .state("SHIFT_OPPORTUNITY_QUESTION")
                .build();
        validChatState = ChatState
                .builder()
                .id(1L)
                .username("Ethan")
                .state("ERROR")
                .build();

        exampleQuestions = Arrays.asList(
                new Question(0L, "ERROR", null, null, "ERROR"),
                new Question(1L, "START", null, null, "START"),
                new Question(2L, "END", "START", "YES", "END")
        );

        exampleQuestionsWithDuplicateNames = Arrays.asList(
                new Question(0L, "ERROR", null, null, "ERROR"),
                new Question(1L, "END", null, null, "START"),
                new Question(2L, "END", "START", "YES", "END")
        );

    }

    @DisplayName("Test reset when username is given")
    @Test
    public void givenUsername_whenReset_thenReturnInitialQuestion() {
        String initialQuestion = decisionTreeService.reset(invalidChatState.getUsername());
        assertThat(initialQuestion).isNotNull();
    }

    @DisplayName("Test getCurrentQuestion when chat state for user does not exist")
    @Test
    public void givenEmptyChatState_whenGetCurrent_thenReturnInitialQuestion() {
        given(chatStateService.getChatState(invalidChatState.getUsername()))
                .willReturn(Optional.empty());
        ReflectionTestUtils.setField(decisionTreeService, "startNode", exampleQuestions.get(1));
        String currentQuestion = decisionTreeService.getCurrentQuestion(invalidChatState.getUsername());
        assertThat(currentQuestion).isEqualTo("START");
    }

    @DisplayName("Test getCurrentQuestion when question does not exist")
    @Test
    public void givenBadQuestion_whenGetCurrent_thenReturnErrorString() {
        given(chatStateService.getChatState(invalidChatState.getUsername()))
                .willReturn(Optional.of(invalidChatState));
        String currentQuestion = decisionTreeService.getCurrentQuestion(invalidChatState.getUsername());
        assertThat(currentQuestion).isEqualTo(decisionTreeService.questionDoesNotExist);
    }

    @DisplayName("Test getCurrentQuestion when question state is valid")
    @Test
    public void givenGoodState_whenGetCurrentQuestion_thenReturnMatchingQuestion() {
        given(chatStateService.getChatState(validChatState.getUsername()))
                .willReturn(Optional.of(validChatState));
        ReflectionTestUtils.setField(decisionTreeService,"decisionTreeNodes", new HashMap<>(){{
            put("START", exampleQuestions.get(1));
            put("ERROR", exampleQuestions.get(0));
        }});
        String currentQuestion = decisionTreeService.getCurrentQuestion(validChatState.getUsername());

        assertThat(currentQuestion).isEqualTo(validChatState.getState());
    }

    @DisplayName("Test getResponse when no state exists for user")
    @Test
    public void givenNoUserState_whenGetResponse_returnStartQuestion() {
        given(chatStateService.getChatState(validChatState.getUsername()))
                .willReturn(Optional.empty());
        ReflectionTestUtils.setField(decisionTreeService, "startNode", exampleQuestions.get(1));

        String currentQuestion = decisionTreeService.getResponse(validChatState.getUsername(), "Yes");

        assertThat(currentQuestion).isEqualTo(exampleQuestions.get(1).getName());
    }

    @DisplayName("Test getResponse when question/state name does not exist in decision tree")
    @Test
    public void givenInvalidState_whenGetResponse_returnQuestionDoesNotExist() {
        given(chatStateService.getChatState(invalidChatState.getUsername()))
                .willReturn(Optional.of(invalidChatState));
        ReflectionTestUtils.setField(decisionTreeService,"decisionTreeNodes", new HashMap<>(){{
            put("START", exampleQuestions.get(1));
            put("ERROR", exampleQuestions.get(0));
        }});

        String currentQuestion = decisionTreeService.getResponse(validChatState.getUsername(), "Yes");

        assertThat(currentQuestion).isEqualTo(decisionTreeService.questionDoesNotExist);
    }

    @DisplayName("Test getResponse when the current question is a terminal node")
    @Test
    public void givenCurrentQuestionIsTerminal_whenGetResponse_returnRestartDialogue() {
        given(chatStateService.getChatState(validChatState.getUsername()))
                .willReturn(Optional.of(validChatState));
        ReflectionTestUtils.setField(decisionTreeService,"decisionTreeNodes", new HashMap<>(){{
            put("START", exampleQuestions.get(1));
            put("ERROR", exampleQuestions.get(0));
        }});
        ReflectionTestUtils.setField(decisionTreeService,"decisionTree", new HashMap<>(){{
            put("START", new HashMap<>());
            put("ERROR", new HashMap<>());
        }});
        ReflectionTestUtils.setField(decisionTreeService,"startNode", exampleQuestions.get(1));

        String currentQuestion = decisionTreeService.getResponse(validChatState.getUsername(), "Yes");

        assertThat(currentQuestion).isEqualTo(decisionTreeService.restartDialogue + exampleQuestions.get(1).getName());
    }

    @DisplayName("Test getResponse when the user response is invalid")
    @Test
    public void givenInvalidUserResponse_whenGetResponse_returnErrorNodeText() {
        given(chatStateService.getChatState(validChatState.getUsername()))
                .willReturn(Optional.of(validChatState));
        ReflectionTestUtils.setField(decisionTreeService,"decisionTreeNodes", new HashMap<>(){{
            put("START", exampleQuestions.get(1));
            put("ERROR", exampleQuestions.get(0));
        }});
        ReflectionTestUtils.setField(decisionTreeService,"decisionTree", new HashMap<>(){{
            put("START", new HashMap<>());
            put("ERROR", new HashMap<>(){{
                put("NO", exampleQuestions.get(2));
            }});
        }});
        ReflectionTestUtils.setField(decisionTreeService,"startNode", exampleQuestions.get(1));
        ReflectionTestUtils.setField(decisionTreeService,"errorNode", exampleQuestions.get(0));

        String currentQuestion = decisionTreeService.getResponse(validChatState.getUsername(), "Yes");

        assertThat(currentQuestion).isEqualTo(exampleQuestions.get(0).getText() + " " + validChatState.getState());
    }

    @DisplayName("Test getResponse when the user response is valid")
    @Test
    public void givenValidUserResponse_whenGetResponse_returnNextQuestion() {
        given(chatStateService.getChatState(validChatState.getUsername()))
                .willReturn(Optional.of(validChatState));
        ReflectionTestUtils.setField(decisionTreeService,"decisionTreeNodes", new HashMap<>(){{
            put("START", exampleQuestions.get(1));
            put("ERROR", exampleQuestions.get(0));
        }});
        ReflectionTestUtils.setField(decisionTreeService,"decisionTree", new HashMap<>(){{
            put("START", new HashMap<>());
            put("ERROR", new HashMap<>(){{
                put("no", exampleQuestions.get(2));
            }});
        }});
        ReflectionTestUtils.setField(decisionTreeService,"startNode", exampleQuestions.get(1));
        ReflectionTestUtils.setField(decisionTreeService,"errorNode", exampleQuestions.get(0));

        String currentQuestion = decisionTreeService.getResponse(validChatState.getUsername(), "no");

        assertThat(currentQuestion).isEqualTo(exampleQuestions.get(2).getText());
    }

    @DisplayName("Test createNodeMap when allQuestions is empty")
    @Test
    public void givenEmptyQuestionsList_whenCreateNodeMap_returnEmptyMap() {
        Map<String,Question> nodeMap = decisionTreeService.createNodeMap(new ArrayList<Question>());

        assertThat(nodeMap.isEmpty()).isTrue();
    }

    @DisplayName("Test createNodeMap when allQuestions have unique names")
    @Test
    public void givenValidQuestions_whenCreateNodeMap_createMap() {
        Map<String,Question> nodeMap = decisionTreeService.createNodeMap(exampleQuestions);
        Map<String,Question> expected = new HashMap<>(){{
            put(exampleQuestions.get(0).getName(), exampleQuestions.get(0));
            put(exampleQuestions.get(1).getName(), exampleQuestions.get(1));
            put(exampleQuestions.get(2).getName(), exampleQuestions.get(2));
        }};

        assertThat(nodeMap).isEqualTo(expected);
    }

    @DisplayName("Test createNodeMap when allQuestions have duplicate names")
    @Test()
    public void givenDuplicateQuestions_whenCreateNodeMap_throwException() {
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            decisionTreeService.createNodeMap(exampleQuestionsWithDuplicateNames);
        });

        assertThat(exception.getMessage()).isEqualTo(decisionTreeService.duplicateNames);
    }

    @DisplayName("Test createDecisionTree when allQuestions is empty")
    @Test
    public void givenEmptyQuestionsList_whenCreateDecisionTree_returnEmptyMap() {
        Map<String,Map<String,Question>> decisionTree = decisionTreeService
                .createDecisionTree(new ArrayList<Question>());

        assertThat(decisionTree.isEmpty()).isEqualTo(true);
    }

    @DisplayName("Test createDecisionTree when allQuestions have unique names")
    @Test
    public void givenValidQuestionsList_whenCreateDecisionTree_createMap() {
        Map<String,Map<String,Question>> decisionTree = decisionTreeService
                .createDecisionTree(exampleQuestions);
        Map<String,Map<String,Question>> expected = new HashMap<>(){{
            put(exampleQuestions.get(0).getName(), new HashMap<>());
            put(exampleQuestions.get(1).getName(), new HashMap<>(){{
                put("yes",exampleQuestions.get(2));
            }});
            put(exampleQuestions.get(2).getName(), new HashMap<>());
        }};

        assertThat(decisionTree).isEqualTo(expected);
    }

    @DisplayName("Test createDecisionTree when allQuestions have duplicate names")
    @Test()
    public void givenDuplicateQuestions_whenCreateDecisionTree_throwException() {
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            decisionTreeService.createDecisionTree(exampleQuestionsWithDuplicateNames);
        });

        assertThat(exception.getMessage()).isEqualTo(decisionTreeService.duplicateNames);
    }
}
