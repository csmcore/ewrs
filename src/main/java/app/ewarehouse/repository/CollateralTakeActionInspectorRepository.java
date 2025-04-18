package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ApplicationCertificateOfCollateral;
import app.ewarehouse.entity.OicCollateralTakeActionInspector;

@Repository
public interface CollateralTakeActionInspectorRepository extends JpaRepository<OicCollateralTakeActionInspector, Integer> {


	OicCollateralTakeActionInspector findByInspectorTeamLeadIdAndColletral(int userId,
			ApplicationCertificateOfCollateral applicantData);

	OicCollateralTakeActionInspector findByColletral(ApplicationCertificateOfCollateral applicantData);

}
