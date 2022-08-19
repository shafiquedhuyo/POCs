package com.example.exportreport.exportreport;

import java.util.ArrayList;
import java.util.List;

public class ExcelWriterTest {

	public static void main(String[] args) {
		ExcelWriter writer = new ExcelWriter();
		List<DataPojo> datas  = new ArrayList<>();
		for (int i = 1; i < 11; i++) {
			DataPojo data = new DataPojo("301126143601"+i,"1436"+i, "10/22/2021", "81230182","1","20","0","0", "12951907209"+i, "863530","xxxxxx", "531095xxxxx430"+i, "10/23/2021", "0108057038380032", null);
			DetailsPojo details = new DetailsPojo("1436"+i, "12951907209"+i, "xxxxxxxx", "301126143601"+i, "10/22/2021", "10/22/2021", "12345","123456789", "1","4848", "0", "434343", "123456789", "P1", "531095xxxxx430"+i);
			data.setDetails(details);
			datas.add(data);
		}

		writer.writeToExcelInMultiSheets("excel.xlsx", datas);
		
	}

}