//
//  Garage.java
//  bidly
//
//  Created by William Shakour (billy1380) on 27 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.bidly.dummy.data;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.Random;
import com.willshex.blogwt.client.page.bidly.dummy.datatypes.Build;
import com.willshex.blogwt.client.page.bidly.dummy.datatypes.Vehicle;
import com.willshex.blogwt.shared.helper.DateTimeHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class Garage {
	public static final List<Vehicle> DUMMY = Arrays.asList(
			(Vehicle) new Vehicle().build(new Build().make("Audi").model("A5"))
					.registration(dummyLicensePlate()).id(1L)
					.created(DateTimeHelper.now()),
			(Vehicle) new Vehicle().build(new Build().make("Audi").model("A3"))
					.registration(dummyLicensePlate()).id(2L)
					.created(DateTimeHelper.now()),
			(Vehicle) new Vehicle().build(new Build().make("VW").model("Golf"))
					.registration(dummyLicensePlate()).id(3L)
					.created(DateTimeHelper.now()),
			(Vehicle) new Vehicle()
					.build(new Build().make("BMW").model("5 Series"))
					.registration(dummyLicensePlate()).id(4L)
					.created(DateTimeHelper.now()),
			(Vehicle) new Vehicle().build(new Build().make("Fiat").model("500"))
					.registration(dummyLicensePlate()).id(5L)
					.created(DateTimeHelper.now()),
			(Vehicle) new Vehicle()
					.build(new Build().make("Nissan").model("Micra"))
					.registration(dummyLicensePlate()).id(6L)
					.created(DateTimeHelper.now()),
			(Vehicle) new Vehicle()
					.build(new Build().make("Range Rover").model("Sport"))
					.registration(dummyLicensePlate()).id(7L)
					.created(DateTimeHelper.now()),
			(Vehicle) new Vehicle()
					.build(new Build().make("Citroen").model("D3"))
					.registration(dummyLicensePlate()).id(8L)
					.created(DateTimeHelper.now()),
			(Vehicle) new Vehicle()
					.build(new Build().make("Mercedes Benz").model("C-class"))
					.registration(dummyLicensePlate()).id(9L)
					.created(DateTimeHelper.now()));

	private static String dummyLicensePlate () {
		return letter(2) + number(2) + " " + letter(3);
	}

	private static String LETTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ";

	private static String letter (int count) {
		StringBuilder letters = new StringBuilder(count);
		for (int i = 0; i < count; i++) {
			letters.append(letter());
		}

		return letters.toString();
	}

	private static char letter () {
		return LETTERS.charAt(Random.nextInt(LETTERS.length()));
	}

	private static String number (int count) {
		StringBuilder numbers = new StringBuilder(count);
		for (int i = 0; i < count; i++) {
			numbers.append(Random.nextInt(9) + 1);
		}
		return numbers.toString();
	}
}
