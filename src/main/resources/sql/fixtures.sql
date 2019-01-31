INSERT INTO user (ID, FIRST_NAME, LAST_NAME, EMAIL) VALUES (NEXT VALUE FOR user_id, 'luck', 'skywalker', 'luke@maytheforcebewithyou.com');
INSERT INTO user (ID, FIRST_NAME, LAST_NAME, EMAIL) VALUES (NEXT VALUE FOR user_id, 'Darth', 'Vador', 'darth@iamyourfather.com');
INSERT INTO user (ID, FIRST_NAME, LAST_NAME, EMAIL) VALUES (NEXT VALUE FOR user_id, 'Leia', 'Organa', 'leia@areyoualittleshortforastormtrooper.com');
INSERT INTO user (ID, FIRST_NAME, LAST_NAME, EMAIL) VALUES (NEXT VALUE FOR user_id, 'Han', 'Solo', 'han@bestpilotinthegalaxy.com');


INSERT INTO category(ID, NAME, ICON, SHORTCUT) VALUES(NEXT VALUE FOR category_id, 'Moving truck', 'icons/icon1.png', 'A');
INSERT INTO category(ID, NAME, ICON, SHORTCUT) VALUES(NEXT VALUE FOR category_id, 'Moving car', 'icons/icon1.png', 'B');
INSERT INTO category(ID, NAME, ICON, SHORTCUT) VALUES(NEXT VALUE FOR category_id, 'Moving bike', 'icons/icon1.png', 'C');
INSERT INTO category(ID, NAME, ICON, SHORTCUT) VALUES(NEXT VALUE FOR category_id, 'Moving bux', 'icons/icon1.png', 'D');

INSERT INTO video(ID, PATH, DURATION) VALUES (NEXT VALUE FOR video_id, 'path/to/video1.mp4', 12345.00);
INSERT INTO video(ID, PATH, DURATION) VALUES (NEXT VALUE FOR video_id, 'path/to/video2.mp4', 12345.00);
INSERT INTO video(ID, PATH, DURATION) VALUES (NEXT VALUE FOR video_id, 'path/to/video3.mp4', 12345.00);
INSERT INTO video(ID, PATH, DURATION) VALUES (NEXT VALUE FOR video_id, 'path/to/video4.mp4', 12345.00);