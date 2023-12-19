package com.rp.sec02;

import java.util.List;

import com.rp.courseutil.Util;
import com.rp.sec02.helper.NameGenerator;

public class Lec07FluxVsList {

	public static void main(String[] args) {

		// Here user has to wait 5 seconds until all 5 names are generated and retrieved
		 List<String> names = NameGenerator.getBlockingNames(5);
		 System.out.println(names);

		 System.out.println("-----------------");
		// Here whenever user name is generated which get retrieved for every 1 second periodically
		// Instead of waiting for 5 seconds to complete the whole process
		NameGenerator.getNames(5).subscribe(Util.onNext());

	}

}
