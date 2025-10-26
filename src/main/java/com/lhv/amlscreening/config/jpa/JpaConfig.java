package com.lhv.amlscreening.config.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.lhv.amlscreening.domain.repository.sanctionedlist.jpa")
public class JpaConfig {}
