package app.ewarehouse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "t_rout_com_insurance")
public class RoutineComplianceInspectorInsurance {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "int_insurance_id")
	    private Long intInsuranceId;

	    @Column(name = "bit_valid_insurance_policy")
	    private Boolean bitValidInsurancePolicy;

	    @Column(name = "vch_insurance_policy_comments")
	    private String vchInsurancePolicyComments;

	    @Column(name = "bit_covers_foreseeable_losses")
	    private Boolean bitCoversForeseeableLosses;

	    @Column(name = "vch_foreseeable_losses_comments")
	    private String vchForeseeableLossesComments;

	    @Column(name = "vch_insurance_name")
	    private String vchInsuranceName;

	    @Column(name = "vch_insurance_policy_number")
	    private String vchInsurancePolicyNumber;

	    @Column(name = "vch_policy_deductible")
	    private String vchPolicyDeductible;

	    @Column(name = "vch_policy_expiration_date")
	    private String vchPolicyExpirationDate;

	    @Column(name = "bit_policy_limits_sufficient")
	    private Boolean bitPolicyLimitsSufficient;

	    @Column(name = "vch_policy_limits_comments")
	    private String vchPolicyLimitsComments;

	    @Column(name = "bit_has_sub_limits")
	    private Boolean bitHasSubLimits;

	    @Column(name = "vch_sub_limits")
	    private String vchSubLimits;

	    @Column(name = "bit_third_party_liability_insurance")
	    private Boolean bitThirdPartyLiabilityInsurance;

	    @Column(name = "vch_third_party_liability_comments")
	    private String vchThirdPartyLiabilityComments;

}
