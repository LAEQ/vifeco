CREATE TABLE IF NOT EXISTS USER (ID int NOT NULL AUTO_INCREMENT PRIMARY KEY, LAST_NAME varchar(30) NOT NULL DEFAULT '', FIRST_NAME varchar(30) NOT NULL DEFAULT '', EMAIL varchar(60) NOT NULL DEFAULT '', IS_DEFAULT boolean DEFAULT FALSE, CREATED_AT timestamp DEFAULT 0, UPDATED_AT timestamp DEFAULT 0, CONSTRAINT USER_UNIQUE UNIQUE(FIRST_NAME, LAST_NAME, EMAIL)) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS CATEGORY (ID int NOT NULL AUTO_INCREMENT PRIMARY KEY, NAME varchar(30) NOT NULL, ICON VARCHAR(30000)  NOT NULL, COLOR VARCHAR(9) NOT NULL, SHORTCUT varchar(1) NOT NULL, CREATED_AT TIMESTAMP DEFAULT NOW(), UPDATED_AT TIMESTAMP DEFAULT NOW() ON UPDATE NOW(), CONSTRAINT category_shortcut_uniq UNIQUE (SHORTCUT)) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS COLLECTION( ID int NOT NULL AUTO_INCREMENT PRIMARY KEY, NAME varchar(30) NOT NULL, IS_DEFAULT boolean DEFAULT FALSE, CREATED_AT TIMESTAMP DEFAULT NOW(), UPDATED_AT TIMESTAMP DEFAULT NOW() ON UPDATE NOW(), CONSTRAINT collection_name_unique UNIQUE (NAME)) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS CATEGORY_COLLECTION( COLLECTION_ID int AUTO_INCREMENT NOT NULL, CATEGORY_ID int NOT NULL, CREATED_AT TIMESTAMP DEFAULT NOW(), UPDATED_AT TIMESTAMP DEFAULT NOW() ON UPDATE NOW(), CONSTRAINT cat_col_key_1 FOREIGN KEY (COLLECTION_ID) REFERENCES COLLECTION (ID) ON DELETE NO ACTION, CONSTRAINT cat_col_key_2 FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY (ID) ON DELETE NO ACTION, CONSTRAINT category_collection_id UNIQUE (COLLECTION_ID, CATEGORY_ID)) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS VIDEO (ID int NOT NULL AUTO_INCREMENT PRIMARY KEY, PATH varchar(30000) NOT NULL, DURATION DOUBLE NOT NULL, USER_ID INT NOT NULL, COLLECTION_ID int NOT NULL, CREATED_AT TIMESTAMP DEFAULT NOW(), UPDATED_AT TIMESTAMP DEFAULT NOW() ON UPDATE NOW(), CONSTRAINT vid_cat_col_id FOREIGN KEY (COLLECTION_ID) REFERENCES COLLECTION (ID) ON DELETE NO ACTION, CONSTRAINT vid_user_col_id FOREIGN KEY (USER_ID) REFERENCES USER (ID) ON DELETE NO ACTION) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS POINT (ID int NOT NULL AUTO_INCREMENT PRIMARY KEY, X double NOT NULL, Y double NOT NULL, VIDEO_ID int NOT NULL, CATEGORY_ID int NOT NULL, START double NOT NULL, CREATED_AT TIMESTAMP DEFAULT NOW(), UPDATED_AT TIMESTAMP DEFAULT NOW() ON UPDATE NOW(), CONSTRAINT point_video FOREIGN KEY (VIDEO_ID) REFERENCES VIDEO (ID) ON DELETE NO ACTION, CONSTRAINT point_category FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY (ID) ON DELETE NO ACTION) ENGINE=InnoDB;