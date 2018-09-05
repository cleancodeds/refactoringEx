package com.assignment11.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//시간대 연령별 통계 클래스
public class Timeslot_Age implements Runnable {
	private String flag;
	private Map<String, String> result = new HashMap<String, String>();
	public static List<Map<String, String>> resultMapList;
	public int teen;
	public int twe;
	public int thir;
	public int fort;
	public int fit;
	public int six;

	public Map<String, String> userData = new HashMap<String, String>();

	public Timeslot_Age(Map<String, String> map, String flag) {
		userData = map;
		result = new HashMap<String, String>();
		resultMapList = new ArrayList<Map<String, String>>();
		teen = 0;
		twe = 0;
		thir = 0;
		fort = 0;
		fit = 0;
		six = 0;

		this.flag = flag;
	}

	private int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);

	}

	public Timeslot_Age() {
	}

	@Override
	public void run() {
		System.out.println("enter: " + flag);
		getTimeslot();
	}

	// 결과값 리턴
	public List<Map<String, String>> returnResult() {
		return resultMapList;
	}

	// 시간대별 접속자 통계
	public void getTimeslot() {
		List<String> periodList = new ArrayList<String>();
		periodList = makePeriod();
		// ageGroup = makeAgeGroup();

		calTimeslotAgeGroupCnt(periodList, "C:\\Users\\Daumsoft\\Desktop\\새 폴더 (2)");
		resultMapList.add(result);
	}

	// 연령별
	public Map<String, String> calTimeslotAgeGroupCnt(List<String> period, String filePath) {
		File dir = new File(filePath);
		File[] fileList = dir.listFiles();
		int fileTimePos = 14;

		try {
			for (int i = 0; i < fileList.length; i++) {
				File file = fileList[i];
				if (file.isFile()) {
					try {
						String line = "";

						FileInputStream fr = new FileInputStream(file);
						BufferedReader br = new BufferedReader(new InputStreamReader(fr, "euc-kr"));
						while ((line = br.readLine()) != null) {
							// TODO rename variable
							String[] token = line.split("\t", -1);

							int fileTimeCode = Integer
									.parseInt(file.getName().substring(fileTimePos + 1, fileTimePos + 3));

							// TODO refactoring parameter code
							// if (Integer.parseInt(period.get(0))>=fileTimeCode&&Integer.parseInt(period.get(5))<=fileTimeCode){
							if (period.get(0).equals(fileTimeCode) || period.get(1).equals(fileTimeCode)
									|| period.get(2).equals(fileTimeCode) || period.get(3).equals(fileTimeCode)
									|| period.get(4).equals(fileTimeCode) || period.get(5).equals(fileTimeCode)) {

								// TODO rename variable
								String userId = token[7];

								// 핵심로직
								if (userData.containsKey(userId)) {
									int resultAge = calUserAge(userId);
									if (resultAge >= 60) {
										six++;
									} else if (resultAge >= 50 && resultAge < 60) {
										fit++;
									} else if (resultAge >= 40 && resultAge < 50) {
										fort++;
									} else if (resultAge >= 30 && resultAge < 40) {
										thir++;
									} else if (resultAge >= 20 && resultAge < 30) {
										twe++;
									} else if (resultAge >= 10 && resultAge < 20) {
										teen++;
									}
								}
							}

						}

						br.close();
					} catch (FileNotFoundException fnfe) {
						fnfe.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else if (file.isDirectory()) {
					System.out.println("#########" + flag + "######디렉토리 이름 = " + file.getName());
					calTimeslotAgeGroupCnt(period, file.getCanonicalPath().toString());
				}
			}
		} catch (IOException e) {
		}

		result.put("flag", flag);
		result.put("teen", String.valueOf(teen));
		result.put("twe", String.valueOf(twe));
		result.put("thir", String.valueOf(thir));
		result.put("fort", String.valueOf(fort));
		result.put("fit", String.valueOf(fit));
		result.put("six", String.valueOf(six));

		// resultMapList.add(result);
		return result;

	}

	private int calUserAge(String userId) {
		int resultAge = getCurrentYear() - extractYear(userId);
		return resultAge;
	}

	private int extractYear(String userId) {
		int year = Integer.parseInt(userData.get(userId).substring(0, 4));
		return year;
	}

	// flag 값에 따라 해당하는 시간대 생성 - 새벽, 아침, 오후, 저녁
	public List<String> makePeriod() {
		List<String> creatPeriod = new ArrayList<String>();
		if (flag.equals("dawn")) {
			creatPeriod.add("01");
			creatPeriod.add("02");
			creatPeriod.add("03");
			creatPeriod.add("04");
			creatPeriod.add("05");
			creatPeriod.add("06");
		} else if (flag.equals("morning")) {
			creatPeriod.add("07");
			creatPeriod.add("08");
			creatPeriod.add("09");
			creatPeriod.add("10");
			creatPeriod.add("11");
			creatPeriod.add("12");
		} else if (flag.equals("afternoon")) {
			creatPeriod.add("13");
			creatPeriod.add("14");
			creatPeriod.add("15");
			creatPeriod.add("16");
			creatPeriod.add("17");
			creatPeriod.add("18");
		} else if (flag.equals("evening")) {
			creatPeriod.add("19");
			creatPeriod.add("20");
			creatPeriod.add("21");
			creatPeriod.add("22");
			creatPeriod.add("23");
			creatPeriod.add("00");
		}
		return creatPeriod;
	}

	// 연령별
	// 시간대별 접속자 통계
	public Map<String, String> notThreadgetTimeslot(String flag) {
		List<String> periodList = new ArrayList<String>();
		periodList = notThreadmakePeriod(flag);
		// ageGroup = makeAgeGroup();
		System.out.println("여기로 안가?????");
		notThreadGetTimeslot_AgeGroupCount(periodList, "C:\\Users\\Daumsoft\\Desktop\\새 폴더 (2)", flag);
		// resultMapList.add(result);
		return result;
	}

	public Map<String, String> notThreadGetTimeslot_AgeGroupCount(List<String> period, String source, String flag) {
		File dir = new File(source);
		File[] fileList = dir.listFiles();
		int index = 14;

		try {
			for (int i = 0; i < fileList.length; i++) {

				File file = fileList[i];
				// System.out.println("\t 파일 이름 = " + file.getName());
				if (file.isFile()) {
					try {
						String line = "";

						FileInputStream fr = new FileInputStream(file);
						BufferedReader br = new BufferedReader(new InputStreamReader(fr, "euc-kr"));
						int age = 0;
						while ((line = br.readLine()) != null) {
							String[] token = line.split("\t", -1);
							try {
								// System.out.println(token[7]);
								if (file.getName().substring(index + 1, index + 3).equals(period.get(0))
										|| file.getName().substring(index + 1, index + 3).equals(period.get(1))
										|| file.getName().substring(index + 1, index + 3).equals(period.get(2))
										|| file.getName().substring(index + 1, index + 3).equals(period.get(3))
										|| file.getName().substring(index + 1, index + 3).equals(period.get(4))
										|| file.getName().substring(index + 1, index + 3).equals(period.get(5))) {
									// System.out.println("\t 파일 이름 = " +
									// file.getName());
									if (userData.containsKey(token[7])) {
										// System.out.println(userData.get(token[7]));

										int year = Integer.parseInt(userData.get(token[7]).substring(0, 4));
										// System.out.println("yewarrrr:
										// "+year);
										int resultAge = getCurrentYear() - year;
										if (resultAge >= 60) {
											six++;
										} else if (resultAge >= 50 && resultAge < 60) {
											fit++;
										} else if (resultAge >= 40 && resultAge < 50) {
											fort++;
										} else if (resultAge >= 30 && resultAge < 40) {
											thir++;
										} else if (resultAge >= 20 && resultAge < 30) {
											twe++;
										} else if (resultAge >= 10 && resultAge < 20) {
											teen++;
										}
										/*
										 * if (year >= 1950 && year < 1960) {
										 * six++; } else if (year >= 1960 &&
										 * year < 1970) { fit++; } else if (year
										 * >= 1970 && year < 1980) { fort++; }
										 * else if (year >= 1980 && year < 1990)
										 * { thir++; } else if (year >= 1990 &&
										 * year < 2000) { twe++; } else if (year
										 * >= 2000 && year < 2011) { teen++; }
										 */
									}
								} else {
								}
							} catch (Exception e) {
								continue;
							}
						}

						br.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else if (file.isDirectory()) {
					System.out.println("#########" + flag + "######디렉토리 이름 = " + file.getName());
					notThreadGetTimeslot_AgeGroupCount(period, file.getCanonicalPath().toString(), flag);
				}
			}
		} catch (IOException e) {
		}

		result.put("flag", flag);
		System.out.println("teen: " + teen);
		result.put("teen", String.valueOf(teen));
		result.put("twe", String.valueOf(twe));
		result.put("thir", String.valueOf(thir));
		result.put("fort", String.valueOf(fort));
		result.put("fit", String.valueOf(fit));
		result.put("six", String.valueOf(six));

		// resultMapList.add(result);
		return result;

	}

	public List<String> notThreadmakePeriod(String flag) {
		List<String> creatPeriod = new ArrayList<String>();
		if (flag.equals("dawn")) {
			creatPeriod.add("01");
			creatPeriod.add("02");
			creatPeriod.add("03");
			creatPeriod.add("04");
			creatPeriod.add("05");
			creatPeriod.add("06");
		} else if (flag.equals("morning")) {
			creatPeriod.add("07");
			creatPeriod.add("08");
			creatPeriod.add("09");
			creatPeriod.add("10");
			creatPeriod.add("11");
			creatPeriod.add("12");
		} else if (flag.equals("afternoon")) {
			creatPeriod.add("13");
			creatPeriod.add("14");
			creatPeriod.add("15");
			creatPeriod.add("16");
			creatPeriod.add("17");
			creatPeriod.add("18");
		} else if (flag.equals("evening")) {
			creatPeriod.add("19");
			creatPeriod.add("20");
			creatPeriod.add("21");
			creatPeriod.add("22");
			creatPeriod.add("23");
			creatPeriod.add("00");
		}
		return creatPeriod;
	}
}
