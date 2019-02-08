INSERT INTO user (ID, FIRST_NAME, LAST_NAME, EMAIL, IS_ACTIVE) VALUES (NEXT VALUE FOR user_id, 'Luck', 'Skywalker', 'luke@maytheforcebewithyou.com', true);
INSERT INTO user (ID, FIRST_NAME, LAST_NAME, EMAIL, IS_ACTIVE) VALUES (NEXT VALUE FOR user_id, 'Darth', 'Vador', 'darth@iamyourfather.com', false);
INSERT INTO user (ID, FIRST_NAME, LAST_NAME, EMAIL, IS_ACTIVE) VALUES (NEXT VALUE FOR user_id, 'Leia', 'Organa', 'leia@areyoualittleshortforastormtrooper.com', false);
INSERT INTO user (ID, FIRST_NAME, LAST_NAME, EMAIL, IS_ACTIVE) VALUES (NEXT VALUE FOR user_id, 'Han', 'Solo', 'han@bestpilotinthegalaxy.com', false);

INSERT INTO category(ID, NAME, ICON, SHORTCUT) VALUES(NEXT VALUE FOR category_id, 'Moving truck', 'icons/icon1.png', 'A');
INSERT INTO category(ID, NAME, ICON, SHORTCUT) VALUES(NEXT VALUE FOR category_id, 'Moving car', 'icons/icon2.png', 'B');
INSERT INTO category(ID, NAME, ICON, SHORTCUT) VALUES(NEXT VALUE FOR category_id, 'Moving bike', 'icons/icon3.png', 'C');
INSERT INTO category(ID, NAME, ICON, SHORTCUT) VALUES(NEXT VALUE FOR category_id, 'Moving bus', 'icons/icon4.png', 'D');

INSERT INTO category_collection(ID, NAME) VALUES(NEXT VALUE FOR category_collection_id, 'Default');
INSERT INTO category_collection(ID, NAME) VALUES(NEXT VALUE FOR category_collection_id, 'Collection 1');
INSERT INTO category_collection(ID, NAME) VALUES(NEXT VALUE FOR category_collection_id, 'Collection 2');

INSERT INTO category_collection_category(CATEGORY_COLLECTION_ID, CATEGORY_ID) VALUES(1, 1);
INSERT INTO category_collection_category(CATEGORY_COLLECTION_ID, CATEGORY_ID) VALUES(1, 2);
INSERT INTO category_collection_category(CATEGORY_COLLECTION_ID, CATEGORY_ID) VALUES(1, 3);
INSERT INTO category_collection_category(CATEGORY_COLLECTION_ID, CATEGORY_ID) VALUES(1, 4);
INSERT INTO category_collection_category(CATEGORY_COLLECTION_ID, CATEGORY_ID) VALUES(2, 2);
INSERT INTO category_collection_category(CATEGORY_COLLECTION_ID, CATEGORY_ID) VALUES(2, 3);
INSERT INTO category_collection_category(CATEGORY_COLLECTION_ID, CATEGORY_ID) VALUES(3, 1);
INSERT INTO category_collection_category(CATEGORY_COLLECTION_ID, CATEGORY_ID) VALUES(3, 3);
INSERT INTO category_collection_category(CATEGORY_COLLECTION_ID, CATEGORY_ID) VALUES(3, 4);

INSERT INTO video(ID, PATH, DURATION) VALUES (NEXT VALUE FOR video_id, 'path/to/video1.mp4', 12345.00);
INSERT INTO video(ID, PATH, DURATION) VALUES (NEXT VALUE FOR video_id, 'path/to/video2.mp4', 12345.00);
INSERT INTO video(ID, PATH, DURATION) VALUES (NEXT VALUE FOR video_id, 'path/to/video3.mp4', 12345.00);
INSERT INTO video(ID, PATH, DURATION) VALUES (NEXT VALUE FOR video_id, 'path/to/video4.mp4', 12345.00);

INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  11, 10, 1, 1, 1, 5555);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  12, 10, 1, 1, 2, 1111);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  13, 10, 1, 1, 1, 3333);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  14, 10, 1, 1, 1, 2222);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  15, 10, 1, 1, 3, 4444);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  16, 10, 2, 1, 1, 1111);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  17, 10, 2, 1, 1, 2222);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  18, 10, 1, 2, 1, 3333);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  19, 10, 1, 2, 2, 1111);
INSERT INTO point(ID, X, Y, VIDEO_ID, USER_ID, CATEGORY_ID, START) VALUES(NEXT VALUE FOR point_id,  20, 10, 1, 2, 3, 2222);