CREATE TABLE api_access_kc_client_usergroup_blacklist (
                                                          api_access_kc_client_id VARCHAR(255) NOT NULL,
                                                          usergroup_blacklist VARCHAR(255)
);

CREATE TABLE api_access_kc_client_user_blacklist (
                                                     api_access_kc_client_id VARCHAR(255) NOT NULL,
                                                     user_blacklist VARCHAR(255)
);

CREATE TABLE sync_kc_client_usergroup_blacklist (
                                                    sync_kc_client_id VARCHAR(255) NOT NULL,
                                                    usergroup_blacklist VARCHAR(255)
);

CREATE TABLE sync_kc_client_user_blacklist (
                                               sync_kc_client_id VARCHAR(255) NOT NULL,
                                               user_blacklist VARCHAR(255)
);

CREATE TABLE win_client_usergroup_blacklist (
                                                win_client_id VARCHAR(255) NOT NULL,
                                                usergroup_blacklist VARCHAR(255)
);

CREATE TABLE win_client_user_blacklist (
                                           win_client_id VARCHAR(255) NOT NULL,
                                           user_blacklist VARCHAR(255)
);

ALTER TABLE api_access_kc_client_usergroup_blacklist
    ADD CONSTRAINT fk_api_access_kc_client_usergroup_blacklist
        FOREIGN KEY (api_access_kc_client_id)
            REFERENCES api_access_kc_client(id);

ALTER TABLE api_access_kc_client_user_blacklist
    ADD CONSTRAINT fk_api_access_kc_client_user_blacklist
        FOREIGN KEY (api_access_kc_client_id)
            REFERENCES api_access_kc_client(id);

ALTER TABLE sync_kc_client_usergroup_blacklist
    ADD CONSTRAINT fk_sync_kc_client_usergroup_blacklist
        FOREIGN KEY (sync_kc_client_id)
            REFERENCES sync_kc_client(id);

ALTER TABLE sync_kc_client_user_blacklist
    ADD CONSTRAINT fk_sync_kc_client_user_blacklist
        FOREIGN KEY (sync_kc_client_id)
            REFERENCES sync_kc_client(id);

ALTER TABLE win_client_usergroup_blacklist
    ADD CONSTRAINT fk_win_client_usergroup_blacklist
        FOREIGN KEY (win_client_id)
            REFERENCES win_client(id);

ALTER TABLE win_client_user_blacklist
    ADD CONSTRAINT fk_win_client_user_blacklist
        FOREIGN KEY (win_client_id)
            REFERENCES win_client(id);
