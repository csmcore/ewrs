package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.TeamMember;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Integer> {

    TeamMember findByIntTeamMemberIdAndBitDeleteFlag(Integer intId, boolean bitDeletedFlag);

    List<TeamMember> findAllByBitDeleteFlag(Boolean bitDeleteFlag);

    Page<TeamMember> findAllByBitDeleteFlag(Boolean bitDeleteFlag, Pageable pageable);
}