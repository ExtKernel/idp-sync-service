CREATE TABLE ipa_client_usergroup_blacklist (
                                                ipa_client_id VARCHAR(255) NOT NULL,
                                                usergroup_blacklist VARCHAR(255)
);

CREATE TABLE ipa_client_user_blacklist (
                                           ipa_client_id VARCHAR(255) NOT NULL,
                                           user_blacklist VARCHAR(255)
);

ALTER TABLE ipa_client_usergroup_blacklist
    ADD CONSTRAINT fk_ipa_client_usergroup_blacklist
        FOREIGN KEY (ipa_client_id)
            REFERENCES ipa_client(id);

ALTER TABLE ipa_client_user_blacklist
    ADD CONSTRAINT fk_ipa_client_user_blacklist
        FOREIGN KEY (ipa_client_id)
            REFERENCES ipa_client(id);

ALTER TABLE ipa_client
    ADD cert_path VARCHAR(255) NOT NULL;