package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.OplSetAuthority;

@Repository
public interface OplSetAuthorityRepository extends JpaRepository<OplSetAuthority, Integer> {
    // You can add custom query methods here if needed
	 OplSetAuthority findByProcessIdAndStageNoAndLabelId(Integer processId, Integer stageNo, Integer labelId);
	 OplSetAuthority findByProcessIdAndStageNoAndRoleIdAndLabelId(Integer processId, Integer stageNo, Integer roleId,Integer labelId);
	// OplSetAuthority findByProcessIdAndStageNoAndLabelId(Integer processId, Integer stageNo,Integer labelId);
}