/**
 * 
 */
package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositorResponse {
	private String intId;
	private String txtName;
	private String txtPassportNo;
	private String txtPassportPath;
	private String intCentralRegistryIdentifier;
	private String txtPostalAddress;
	private String txtTelephoneNumber;
	private String txtEmailAddress;
	private String txtWard;
	private SubCountyResponse subCounty;
	private Integer intBankAccNo;
	private String txtBankName;
	private String txtBankProofPath;
	private Integer intBusinessRegNo;
	private String txtBusinessRegCertPath;
	private Date dtmCreatedOn;
	private Date stmUpdatedOn;
	private Integer intCreatedBy;
	private Integer intUpdatedBy;
	private String enmStatus;
	private Integer intApprovedBy;
}
