package com.example.cep_api.repository;

import com.example.cep_api.model.Cep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CepRepository extends JpaRepository <Cep, Long>{}
