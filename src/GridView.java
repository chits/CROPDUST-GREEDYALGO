/*
 * All the calculations are done in this class.
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class GridView extends JFrame {
	
	enum Day{
		Monday,
		Tuesday,
		Done
	}
	
	static List<DustStation> allStations;
	static DustStation home = new DustStation(false,new Point(20, 25));
	static DustStation base = new DustStation(false,new Point(70, 80));
	
	static final float distInOneMin = 1.67f; 
	static final float fuelPerMin = 0.17f; 
	
	static final float fuelForDusting = 15;
	static final int timeForDusting = 90;
	
	static int visitToBase;
	
	static float totalTime;
	static float totalFuel;
	static float currentFuel;
	static float totalChemical;
	static float currentChemical;
	static float totalDistance;
	static float totalCostChemical;
	static float totalCostFuel;
	static float totalCostPlane;
	static float totalCostPilot;
	static float totalPilotTime;
	
	static float totalTimeMonday;
	static float totalFuelMonday;
	static float totalChemicalMonday;
	static float totalDistanceMonday;
	static float totalCostMonday;
	static float totalFuelCostMonday;
	static float totalChemicalCostMonday;
	static float totalPilotTimeMonday;
	
	static float totalTimeTuesday;
	static float totalFuelTuesday;
	static float totalChemicalTuesday;
	static float totalDistanceTuesday;
	static float totalCostTuesday;
	static float totalFuelCostTuesday;
	static float totalChemicalCostTuesday;
	static float totalPilotTimeTuesday;
	
	static Day presentDay = Day.Monday;
	
	DustStation presentStation;
	
	GridCell gridCell;
	
	JLabel totalQuant, currentQuant;
	
	GridView(int height, int width)
	{
		setTitle("Crop Duster");
		this.setPreferredSize(new Dimension(800,800));
		setLocation(300, 200);
		
		allStations = new ArrayList<>();
		
		gridCell = new GridCell(height, width);
		totalQuant = new JLabel("", JLabel.LEFT);
		currentQuant = new JLabel("", JLabel.LEFT);

        Container contents = getContentPane();
        contents.add(gridCell, BorderLayout.CENTER);
        contents.add(totalQuant, BorderLayout.NORTH);
        contents.add(totalQuant, BorderLayout.NORTH);
        contents.add(currentQuant, BorderLayout.SOUTH);
        pack();
        setVisible(true);
        
        gridCell.preparePaint();
        
        for(int row = 0; row < 100; row++) {
            for(int col = 0; col < 100; col++) {
                gridCell.drawMark(col, row, Color.WHITE);
            }
        }
        gridCell.repaint();
        
        Init(gridCell, presentDay);
        
        setLabels();
        while(presentDay != Day.Done){
        	while(allStations.size() != 0)
            {
            	getNextStation(presentStation);
            	try {
    				Thread.sleep(5000);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
            }
        	if(presentDay == Day.Tuesday)
        	{
        		presentDay = Day.Done;
        		totalTimeTuesday = totalTime;
            	totalFuelTuesday = totalFuel;
            	totalChemicalTuesday = totalChemical;
            	totalDistanceTuesday = totalDistance;
            	
            	totalCostPlane = totalTime * 2;
            	totalPilotTime = totalTime + (visitToBase * 45);
            	totalCostPilot =  (totalPilotTime * 10)/6;
            	
            	totalFuelCostTuesday = totalCostFuel;
            	totalChemicalCostTuesday = totalCostChemical;
            	totalPilotTimeTuesday = totalPilotTime;
            	
            	totalCostTuesday = totalCostPlane+ totalCostPilot + totalCostChemical +totalCostFuel;
            	setLabels();
            	gridCell.repaint();
        	}else{
        		presentDay = Day.Tuesday;
            	totalTimeMonday = totalTime;
            	totalFuelMonday = totalFuel;
            	totalChemicalMonday = totalChemical;
            	totalDistanceMonday = totalDistance;
            	
            	totalCostPlane = totalTime * 2;
            	totalPilotTime = totalTime + (visitToBase * 45);
            	totalCostPilot =  (totalPilotTime * 10)/6;
            	
            	totalFuelCostMonday = totalCostFuel;
            	totalChemicalCostMonday = totalCostChemical;
            	totalPilotTimeMonday = totalPilotTime;
            	
            	totalCostMonday = totalCostPlane+ totalCostPilot + totalCostChemical +totalCostFuel;
            	
            	setLabels();
            	gridCell.repaint();
            	
            	gridCell.clearAll();
            	
            	for(int row = 0; row < 100; row++) {
                    for(int col = 0; col < 100; col++) {
                        gridCell.drawMark(col, row, Color.WHITE);
                    }
                }
            	Init(gridCell, presentDay);
        	}
        }
        float totalCostForBothDays = totalCostMonday + totalCostTuesday;
        float profit = 13500 - totalCostForBothDays;
        JOptionPane.showMessageDialog(null, "<html>Total Cost for both days :$" + totalCostForBothDays
        		+"<br>Total Fuel cost : $" + (totalFuelCostMonday + totalFuelCostTuesday) + 
        		", Total Chemical cost : $" + (totalChemicalCostMonday + totalChemicalCostTuesday)
        		+"<br>Total Pilot hours : " + (totalPilotTimeMonday + totalPilotTimeTuesday)/60 + " hrs"+
        		", Total Plane hours : " + (totalTimeMonday+totalTimeTuesday)/60 + " hrs" +
        		"<br> Total Time of Pilot : " + totalPilotTime/60 +" hrs</html>");
	}
	
	void setLabels()
	{
		Day day = (presentDay == Day.Done)?Day.Tuesday:presentDay ;
		totalQuant.setText("<html><strong>"+ day + "</strong>  <br>Time : " + calculateTime(totalTime/60) + " " +
				"<br>Total Fuel used : " + totalFuel + " G, Total Chemical used : " + totalChemical + " G" +
        		"<br>Total Distance flown : " + totalDistance + " miles, Total Flight time :" + totalTime/60 + " hrs</html>");
        
        currentQuant.setText("<html>Current Fuel : " + currentFuel + " G, Current Chemical : " + currentChemical+ " G" +
        		"<br>Total Chemical cost : $" + totalCostChemical + " Total Fuel cost : $" + totalCostFuel +
        		"<br><strong>Current Co-ordinate "+ presentStation.getCordinates().getX() + ", " + 
        		presentStation.getCordinates().getY() +"</strong></html>");
	}
	
	String calculateTime(float time)
	{
		return time + "hrs since 6 am";
	}
	/*
	 * Initilaize the frame at each day
	 */
	void Init(GridCell gridCell, Day presentDay)
	{
		gridCell.drawMark(20,  25, Color.GREEN);
		gridCell.drawMark(70,  80, Color.GREEN);
		
		home.setBase();
		base.setBase();
		
		if(presentDay == Day.Monday){
			allStations.add(new DustStation(false, new Point(84,90)));
			allStations.add(new DustStation(false, new Point(20,79)));
			allStations.add(new DustStation(false, new Point(34,89)));
			allStations.add(new DustStation(false, new Point(50,67)));
			allStations.add(new DustStation(false, new Point(95,64)));
			allStations.add(new DustStation(false, new Point(51,31)));
			allStations.add(new DustStation(false, new Point(1,71)));
			allStations.add(new DustStation(false, new Point(43,43)));
			allStations.add(new DustStation(false, new Point(78,88)));
			
			gridCell.drawMark(84,  90, Color.RED);
			gridCell.drawMark(20,  79, Color.RED);
			gridCell.drawMark(34,  89, Color.RED);
			gridCell.drawMark(50,  67, Color.RED);
			gridCell.drawMark(95,  64, Color.RED);
			gridCell.drawMark(51,  31, Color.RED);
			gridCell.drawMark(1,  71, Color.RED);
			gridCell.drawMark(43,  43, Color.RED);
			gridCell.drawMark(78,  88, Color.RED);
		}else{
			allStations.add(new DustStation(false, new Point(25,12)));
			allStations.add(new DustStation(false, new Point(72,48)));
			allStations.add(new DustStation(false, new Point(87,94)));
			allStations.add(new DustStation(false, new Point(89,76)));
			allStations.add(new DustStation(false, new Point(6,27)));
			allStations.add(new DustStation(false, new Point(36,72)));
			allStations.add(new DustStation(false, new Point(30,38)));
			allStations.add(new DustStation(false, new Point(72,59)));
			allStations.add(new DustStation(false, new Point(29,80)));
			
			gridCell.drawMark(25,12, Color.RED);
			gridCell.drawMark(72,48, Color.RED);
			gridCell.drawMark(87,94, Color.RED);
			gridCell.drawMark(89,76, Color.RED);
			gridCell.drawMark(6,27, Color.RED);
			gridCell.drawMark(36,72, Color.RED);
			gridCell.drawMark(30,38, Color.RED);
			gridCell.drawMark(72,59, Color.RED);
			gridCell.drawMark(29,80, Color.RED);
		}
		gridCell.repaint();
		
		presentStation = home;

		visitToBase = 0;
		currentFuel = 50;
		currentChemical = 100;
		
		totalCostChemical = 1600;
		totalCostFuel = 250;
		totalCostPlane = 0;
		totalCostPilot = 0;
		totalPilotTime = 0;
		totalChemical = 0;
		totalDistance = 0;
		totalTime = 0;
		totalFuel = 0;
	}
	/*
	 * Find nearest base from the current station
	 */
	public DustStation findNearestBase(DustStation p)
	{
		float distanceFromHome = findDist(p.getCordinates(), home.getCordinates());
		float distanceFromBase = findDist(p.getCordinates(), base.getCordinates());
		return (distanceFromHome < distanceFromBase) ? home : base;
	}
	/*
	 * Calculate the fuel required to go to and dust next station
	 */
	public float calculateFuelRequired(DustStation d)
	{
		DustStation nearest = findNearestNeighbour(d.getCordinates());
		float distanceFromAtoB = findDist(d.getCordinates(),nearest.getCordinates());
		
		int timeFromAtoB = (int)(distanceFromAtoB * distInOneMin);
		float fuelFromAtoB = timeFromAtoB * fuelPerMin;
		
		float totalTime = timeFromAtoB + timeForDusting;
		float totalFuelRequired = fuelFromAtoB + fuelForDusting;
		
		return totalFuelRequired;
	}
	/*
	 * Calculate the total time to go to the station and dust it
	 */
	public int totalTimeFromStation(DustStation d)
	{
		DustStation nearest = findNearestNeighbour(d.getCordinates());
		float distanceFromAtoB = findDist(d.getCordinates(),nearest.getCordinates());
		
		int timeFromAtoB = (int)(distanceFromAtoB * distInOneMin);
		
		int totalTime = timeFromAtoB + timeForDusting;
		
		return totalTime;
	}
	/*
	 * Total time to base
	 */
	public int totalTimeFromBase(DustStation d)
	{
		DustStation nearest = findNearestBase(d);
		float distanceFromAtoB = findDist(d.getCordinates(),nearest.getCordinates());
		
		int timeToBase = (int)(distanceFromAtoB * distInOneMin);
		
		return timeToBase;
	}
	/*
	 * calculate the fuel required to go to base
	 */
	public float calculateFuelRequiredForBase(DustStation d)
	{
		DustStation nearest = findNearestBase(d);
		float distanceFromAtoB = findDist(d.getCordinates(),nearest.getCordinates());
		
		int timeFromAtoB = (int)(distanceFromAtoB * distInOneMin);
		float fuelFromAtoB = timeFromAtoB * fuelPerMin;
		
		return fuelFromAtoB;
	}
	/*
	 * method to go to next station or base
	 */
	public void getNextStation(DustStation d)
	{
		float fuelToNearestNeighbor = calculateFuelRequired(d);
		float distance;
		int time;
		if(currentFuel > fuelToNearestNeighbor){
			float fueltoBaseFromNeighbor = calculateFuelRequiredForBase(findNearestNeighbour(d.getCordinates()));
			if(currentFuel - fuelToNearestNeighbor >= fueltoBaseFromNeighbor){
				time = totalTimeFromStation(d);
				distance = findDist(d.getCordinates(),findNearestNeighbour(d.getCordinates()).getCordinates());
				gridCell.drawLine(d.getCordinates(), findNearestNeighbour(d.getCordinates()).getCordinates(),Color.BLUE);
				goToNearestStation(findNearestNeighbour(d.getCordinates()), fuelToNearestNeighbor, time, distance);
			}
			else{
				time = totalTimeFromBase(d);
				distance = findDist(d.getCordinates(),findNearestBase(d).getCordinates());
				gridCell.drawLine(d.getCordinates(), findNearestBase(d).getCordinates(), Color.RED);
				goToNearestStation(findNearestBase(d),calculateFuelRequiredForBase(d), time, distance);
			}
		}else{
			time = totalTimeFromBase(d);
			distance = findDist(d.getCordinates(),findNearestBase(d).getCordinates());
			goToNearestStation(findNearestBase(d),calculateFuelRequiredForBase(d), time, distance);
			gridCell.drawLine(d.getCordinates(), findNearestBase(d).getCordinates(),Color.RED);
		}
		setLabels();
		gridCell.repaint();
	}
	/*
	 * go to nearest station
	 */
	public void goToNearestStation(DustStation d, float fuel, int time, float distance)
	{
		if(!d.IsBase())
		{
			currentFuel -= fuel;
			currentChemical -= 25;
			totalChemical += 25;
			d.setDusted();
			allStations.remove(d);
		}else{
			if(d.getCordinates().getX() == 20)
			{
				totalCostChemical += (100 - currentChemical)*16;
				totalCostFuel += (50-currentFuel)*5;
			}else{
				totalCostChemical += (100 - currentChemical)*15;
				totalCostFuel += (50-currentFuel)*6;
			}
			currentFuel = ((currentFuel - fuel) + 50) < 50 ?(currentFuel - fuel) + 50 : 50;
			currentChemical = (currentChemical + 100) < 100 ?(currentChemical + 100) : 100;
			visitToBase++;
		}
		totalFuel += fuel;
		totalTime += time;
		totalDistance += distance;
		presentStation = d;
	}
	/*
	 * calculate distance between two points
	 */
	float findDist(Point p1, Point p2) 
	{
		float dx = p2.getX() - p1.getX();
		float dy = p2.getY() - p1.getY();
		return (float)Math.sqrt(dx * dx + dy * dy);
	}
	/*
	 * find nearest neighbour
	 */
	public DustStation findNearestNeighbour(Point p)
	{
		float minDistance = 100;
		DustStation nearest = null;
		for(int i = 0 ; i < allStations.size(); i++)
		{
			float distanceFromP = findDist(p,allStations.get(i).getCordinates());
			if(distanceFromP < minDistance)
			{
				minDistance = distanceFromP;
				nearest = allStations.get(i);
			}
		}
		return nearest;
	}

}
