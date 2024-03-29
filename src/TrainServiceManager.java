import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import java.util.TreeSet;

public class TrainServiceManager {
	FileReader fileReader;
	BufferedReader reader;
	final int TR1MIN = 1;
	final int TR1MAX = 10;
	final int TR2MIN = 11;
	final int TR2MAX = 20;

	public static void main(String[] args) throws TrainServiceException {
		System.out.println("Program started from Main");
		TrainServiceManager t = new TrainServiceManager();
		t.getTrainDetails("D:\\TrainRoutesAndFares.dat", 1, 9, "2019/9/29");
		t.getTrainDetails("D:\\TrainRoutesAndFares.dat", 3, 9, "2019/9/30").toString();
		// System.out.println(t.getTrainDetails("D:\\TrainRoutesAndFares.dat", 11, 17,
		// "2019/9/28").toString());
		// System.out.println(t.getTrainDetails("D:\\TrainRoutesAndFares.dat", 12, 20,
		// "2019/9/30").toString());

	}

	/* Return the list of trains for the given parameter */
	public List<TrainDetailsVO> getTrainDetails(final String filePath, int source, int destination, String dateOfTravel)
			throws TrainServiceException {

		if (source < TR1MIN || destination > TR2MAX) {
			throw new TrainServiceException("Train route source/destination input does not match the master inventory");
		}

		if (source == destination)
			throw new TrainServiceException("Train source/destination are same");
		SimpleDateFormat formatter = null;
		formatter = new SimpleDateFormat("yyyy/MM/dd");
		formatter.setLenient(false);
		System.out.println(dateOfTravel);
		Date tempDateOfTravel = null;
		int dayOfWeek = 0;
		char special = 'Y';

		try {
			tempDateOfTravel = formatter.parse(dateOfTravel);
		} catch (ParseException e) {
			throw new TrainServiceException("Invalid date input");
		}

		System.out.println(tempDateOfTravel.compareTo(new Date()));

		if (tempDateOfTravel.compareTo(new Date()) < 0) {
			throw new TrainServiceException("Travel Date should always be greater than current date");
		}

		Calendar c = Calendar.getInstance();
		c.setTime(tempDateOfTravel);

		dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		special = (dayOfWeek == 1 ? 'Y' : 'N');
		System.out.println("Day Of Week " + dayOfWeek);
		System.out.println(special);

		TrainDetailsVO trainDetailsVO = null;

		List<TrainDetailsVO> trainDetailsVOList = new ArrayList<TrainDetailsVO>();
		String currLine = null;
		String[] tempArray = null;

		try {
			fileReader = new FileReader(filePath);
			reader = new BufferedReader(fileReader);

			while ((currLine = (reader.readLine())) != null) {
				tempArray = currLine.split(",");
				int tempSource = Integer.parseInt(tempArray[2]);
				System.out.println("source Station" + tempSource);
				int tempDestination = Integer.parseInt(tempArray[3]);
				System.out.println("destination Station" + tempDestination);
				char tempSpecial = tempArray[4].toCharArray()[0];
				System.out.println("special or not from dat file " + tempSpecial);
				char fullSearch = 'Y';
				if (special == 'Y') {
					fullSearch = 'N';
				}
				// else fullSearch='Y';

				System.out.println("Full search " + fullSearch);

				if (tempSource == source && tempDestination == destination) {
					System.out.println("TempSource : " + tempSource + " and source : " + source);
					System.out.println("TempDestination : " + tempDestination + " and Destination : " + destination);

					if ((fullSearch == 'Y') ? true : (tempSpecial == special)) {

						trainDetailsVO = new TrainDetailsVO();
						trainDetailsVO.setTrainNumber(tempArray[0]);
						trainDetailsVO.setRoute(tempArray[1]);
						trainDetailsVO.setSource(tempSource);
						trainDetailsVO.setDestination(tempDestination);
						trainDetailsVO.setSpecial(tempSpecial);
						trainDetailsVO.setDateOfTravel(tempDateOfTravel);
						System.out.println(trainDetailsVO.toString());
						trainDetailsVOList.add(trainDetailsVO);
					}
				}
			}

		} catch (FileNotFoundException e) {
			throw new TrainServiceException("File Not Found" + e.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(trainDetailsVOList.toString());
		return trainDetailsVOList;
	}

	/* Return the special trains */
	public Map getTrainSchedule(String filePath) throws TrainServiceException {

		String currLine = null;
		String[] tempArray = null;

		TreeSet<Integer> trainNumbers = new TreeSet<Integer>();
		TreeMap<Integer, TreeSet<Integer>> specialTrains = new TreeMap<Integer, TreeSet<Integer>>();

		try {
			fileReader = new FileReader(filePath);
			reader = new BufferedReader(fileReader);

			while ((currLine = (reader.readLine())) != null) {
				tempArray = currLine.split(",");

				if (tempArray[4].equals("Y"))
					trainNumbers.add(Integer.parseInt(tempArray[0]));
			}
		} catch (FileNotFoundException e) {
			throw new TrainServiceException("File Not Found" + e.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		specialTrains.put(1, trainNumbers);
		return specialTrains;

	}

}

