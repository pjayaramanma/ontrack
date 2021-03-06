DROP TABLE GLOBAL_AUTHORIZATIONS;
DROP TABLE PROJECT_AUTHORIZATIONS;

CREATE TABLE GLOBAL_AUTHORIZATIONS (
  ACCOUNT INTEGER     NOT NULL,
  ROLE    VARCHAR(80) NOT NULL,
  CONSTRAINT GLOBAL_AUTHORIZATIONS_PK PRIMARY KEY (ACCOUNT, ROLE),
  CONSTRAINT GLOBAL_AUTHORIZATIONS_FK_ACCOUNT FOREIGN KEY (ACCOUNT) REFERENCES ACCOUNTS (ID)
    ON DELETE CASCADE
);

CREATE TABLE PROJECT_AUTHORIZATIONS (
  ACCOUNT INTEGER     NOT NULL,
  PROJECT INTEGER     NOT NULL,
  ROLE    VARCHAR(80) NOT NULL,
  CONSTRAINT PROJECT_AUTHORIZATIONS_PK PRIMARY KEY (ACCOUNT, PROJECT, ROLE),
  CONSTRAINT PROJECT_AUTHORIZATIONS_FK_ACCOUNT FOREIGN KEY (ACCOUNT) REFERENCES ACCOUNTS (ID)
    ON DELETE CASCADE,
  CONSTRAINT PROJECT_AUTHORIZATIONS_FK_PROJECT FOREIGN KEY (PROJECT) REFERENCES PROJECTS (ID)
    ON DELETE CASCADE
);

CREATE TABLE ACCOUNT_GROUPS (
  ID          INTEGER      NOT NULL AUTO_INCREMENT,
  NAME        VARCHAR(40)  NOT NULL,
  DESCRIPTION VARCHAR(300) NULL,
  CONSTRAINT ACCOUNT_GROUPS_PK PRIMARY KEY (ID),
  CONSTRAINT ACCOUNT_GROUPS_UQ_NAME UNIQUE (NAME)
);

CREATE TABLE GROUP_GLOBAL_AUTHORIZATIONS (
  ACCOUNTGROUP INTEGER     NOT NULL,
  ROLE         VARCHAR(80) NOT NULL,
  CONSTRAINT GROUP_GLOBAL_AUTHORIZATIONS_PK PRIMARY KEY (ACCOUNTGROUP, ROLE),
  CONSTRAINT GROUP_GLOBAL_AUTHORIZATIONS_FK_ACCOUNT FOREIGN KEY (ACCOUNTGROUP) REFERENCES ACCOUNT_GROUPS (ID)
    ON DELETE CASCADE
);

CREATE TABLE GROUP_PROJECT_AUTHORIZATIONS (
  ACCOUNTGROUP INTEGER     NOT NULL,
  PROJECT      INTEGER     NOT NULL,
  ROLE         VARCHAR(80) NOT NULL,
  CONSTRAINT GROUP_PROJECT_AUTHORIZATIONS_PK PRIMARY KEY (ACCOUNTGROUP, PROJECT, ROLE),
  CONSTRAINT GROUP_PROJECT_AUTHORIZATIONS_FK_ACCOUNT FOREIGN KEY (ACCOUNTGROUP) REFERENCES ACCOUNT_GROUPS (ID)
    ON DELETE CASCADE,
  CONSTRAINT GROUP_PROJECT_AUTHORIZATIONS_FK_PROJECT FOREIGN KEY (PROJECT) REFERENCES PROJECTS (ID)
    ON DELETE CASCADE
);

CREATE TABLE ACCOUNT_GROUP_LINK (
  ACCOUNT      INTEGER NOT NULL,
  ACCOUNTGROUP INTEGER NOT NULL,
  CONSTRAINT ACCOUNT_GROUP_LINK_PK PRIMARY KEY (ACCOUNT, ACCOUNTGROUP),
  CONSTRAINT ACCOUNT_GROUP_LINK_FK_ACCOUNT FOREIGN KEY (ACCOUNT) REFERENCES ACCOUNTS (ID)
    ON DELETE CASCADE,
  CONSTRAINT ACCOUNT_GROUP_LINK_FK_ACCOUNTGROUP FOREIGN KEY (ACCOUNTGROUP) REFERENCES ACCOUNT_GROUPS (ID)
    ON DELETE CASCADE
);
