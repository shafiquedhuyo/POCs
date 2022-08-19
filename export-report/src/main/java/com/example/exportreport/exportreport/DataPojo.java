package com.example.exportreport.exportreport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataPojo {
	private String retailerId;
	private String merchandId;
	private String transactionDate;
	private String terminalId;
	private String numOfTransactions;
	private String transactionAmount;
	private String numOfReversalTransaction;
	private String reversalTransactionAmount;
	private String transSeqNumber;
	private String transApprovalCd;
	private String retailerName;
	private String cardName;
	private String postingDate;
	private String retailerPosAmount;
	private DetailsPojo details;

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class DetailsPojo {

	private String merchandId;
	private String retailerPosAmount;
	private String retailerName;
	private String retailerId;
	private String transactionDate;
	private String postingDate;
	private String transactionTime;
	private String terminalId;
	private String numOfTransactions;
	private String transactionAmount;
	private String transApprovalCd;
	private String transSeqNumber;
	private String posDescription;
	private String cardType;
	private String cardNumber;

}
