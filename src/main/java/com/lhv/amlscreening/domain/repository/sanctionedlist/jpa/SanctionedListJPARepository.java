package com.lhv.amlscreening.domain.repository.sanctionedlist.jpa;

import com.lhv.amlscreening.domain.entity.SanctionedListEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SanctionedListJPARepository extends JpaRepository<SanctionedListEntity, UUID> {}
