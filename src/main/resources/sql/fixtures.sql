INSERT INTO user (ID, FIRST_NAME, LAST_NAME, EMAIL) VALUES (NEXT VALUE FOR user_id, 'luck', 'skywalker', 'luke@maytheforcebewithyou.com');
INSERT INTO user (ID, FIRST_NAME, LAST_NAME, EMAIL) VALUES (NEXT VALUE FOR user_id, 'Darth', 'Vador', 'darth@iamyourfather.com');
INSERT INTO user (ID, FIRST_NAME, LAST_NAME, EMAIL) VALUES (NEXT VALUE FOR user_id, 'Leia', 'Organa', 'leia@areyoualittleshortforastormtrooper.com');
INSERT INTO user (ID, FIRST_NAME, LAST_NAME, EMAIL) VALUES (NEXT VALUE FOR user_id, 'Han', 'Solo', 'han@bestpilotinthegalaxy.com');


INSERT INTO category(ID, NAME, ICON, SHORTCUT) VALUES(NEXT VALUE FOR category_id, 'Moving truck', 'icons/icon1.png', 'A');
INSERT INTO category(ID, NAME, ICON, SHORTCUT) VALUES(NEXT VALUE FOR category_id, 'Moving car', 'icons/icon2.png', 'B');
INSERT INTO category(ID, NAME, ICON, SHORTCUT) VALUES(NEXT VALUE FOR category_id, 'Moving bike', 'icons/icon3.png', 'C');
INSERT INTO category(ID, NAME, ICON, SHORTCUT) VALUES(NEXT VALUE FOR category_id, 'Moving bus', 'icons/icon4.png', 'D');

INSERT INTO video(ID, PATH, DURATION) VALUES (NEXT VALUE FOR video_id, 'path/to/video1.mp4', 12345.00);
INSERT INTO video(ID, PATH, DURATION) VALUES (NEXT VALUE FOR video_id, 'path/to/video2.mp4', 12345.00);
INSERT INTO video(ID, PATH, DURATION) VALUES (NEXT VALUE FOR video_id, 'path/to/video3.mp4', 12345.00);
INSERT INTO video(ID, PATH, DURATION) VALUES (NEXT VALUE FOR video_id, 'path/to/video4.mp4', 12345.00);

INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  10, 10, 1, 1, 1, 5);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  10, 10, 1, 1, 2, 1);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  10, 10, 1, 1, 1, 3);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  10, 10, 1, 1, 1, 2);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  10, 10, 1, 1, 3, 4);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  10, 10, 2, 1, 1, 1);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  10, 10, 2, 1, 1, 1);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  10, 10, 1, 2, 1, 3);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  10, 10, 1, 2, 1, 1);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  10, 10, 1, 2, 1, 1);
