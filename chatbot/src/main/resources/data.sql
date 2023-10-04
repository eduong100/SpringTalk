INSERT INTO questions (id, name, parent, previous_decision, text) VALUES
  (0,'ERROR', null, null, 'I''m sorry, I didn''t understand your response. I will restate my previous message: '),
  (1,'SHIFT_OPPORTUNITY_QUESTION', null, null, 'Hi! This is LISA. I have a great shift opportunity for you! Are you interested in hearing about it? Please respond with "Yes" or "No".'),
  (2,'SHIFT_DECLINE_QUESTION', 'SHIFT_OPPORTUNITY_QUESTION', 'No','Ok, thanks. Can you let me know why not? Respond 1: Too far. Respond 2: Not available. Respond 3: Other.'),
  (3,'SHIFT_ACCEPT', 'SHIFT_OPPORTUNITY_QUESTION', 'Yes', 'Great! The shift is at 1313 Mockingbird Ln at 2/15/2021 4:00pm-12:00am. We will see you there!'),
  (4,'SHIFT_TOO_FAR', 'SHIFT_DECLINE_QUESTION', '1', 'Thanks for letting us know. I will avoid offering shifts at this location in the future.'),
  (5,'SHIFT_NOT_AVAILABLE', 'SHIFT_DECLINE_QUESTION', '2', 'Thanks for letting me know. I will avoid offering shifts at this time in the future'),
  (6,'SHIFT_OTHER', 'SHIFT_DECLINE_QUESTION', '3', 'Ok. Thanks. I will not offer shifts at this location or time in the future.');