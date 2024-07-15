package com.iliauni.idpsyncservice.repository;

import com.iliauni.idpsyncservice.model.IpaClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IpaClientRepository extends JpaRepository<IpaClient, String> {
}
