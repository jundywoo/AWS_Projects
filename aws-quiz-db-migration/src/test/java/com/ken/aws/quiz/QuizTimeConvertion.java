package com.ken.aws.quiz;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

public class QuizTimeConvertion {

	public static void main(String[] args) throws Exception {
		do {
			System.out.print("Time=");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String time = reader.readLine();
			if (StringUtils.isBlank(time)) {
				break;
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd (HH:mm:ss.SSS) ZZZ");
			System.out.println(formatter.parse(time).getTime());
		} while (true);
	}

}
