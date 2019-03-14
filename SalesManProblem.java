import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;

public class SalesManProblem {
	public static void main(String[] args) {
		double[][] arr;
		ArrayList<Integer> optimal = new ArrayList<>();
		optimal = TSP.ReadIntegerFile("C:\\Users\\micha\\Documents\\lab15\\TSP_48_OPT.txt");
				arr = TSP.ReadArrayFile("C:\\Users\\micha\\Documents\\lab15\\TSP_48.txt", " ");
		SalesManSolution optimal_salesman = new SalesManSolution(optimal);
		System.out.println("OPTIMAL :" + optimal_salesman.SalesManFitness(arr));
		
		ArrayList<Double> fitnesses = new ArrayList<>();
		for (int i=0; i<10; i++){
			SalesManSolution salesMAN = SalesManSolutionRHMC(arr, 25000);
			fitnesses.add(salesMAN.SalesManFitness(arr));
		}
		System.out.println("average RHMC:" + calculateAverage(fitnesses));
	}
	
	public static double calculateAverage(ArrayList<Double> marks) {
		double sum = 0.0;
		for(int i=0; i<marks.size();i++){
			sum = sum + marks.get(i);
		}
		return sum / marks.size();
	}
	
	public static SalesManSolution SalesManSolutionRHMC(double[][] weights, int iter){
		int size = weights.length;
		SalesManSolution tour = new SalesManSolution(size);
		double fitness= 0.0;
		for(int i=0; i<iter; i++){
			SalesManSolution oldTour = new SalesManSolution(tour.getTour());
			double oldFitness = oldTour.SalesManFitness(weights);
			tour.smallChange();
			double newFitness = tour.SalesManFitness(weights);
			fitness = newFitness;
			if (newFitness>oldFitness){
				tour=new SalesManSolution(oldTour.getTour());
				fitness=oldFitness;
			}
		}
		//System.out.println("best fitness :" + fitness);
		return tour;
	}
}

class SalesManSolution {
	ArrayList<Integer> tour;
	ArrayList<Integer> previous_tour;
	
	public SalesManSolution(int n){
		tour = randomPermutation(n);
	}
	
	public SalesManSolution(ArrayList<Integer> list){
        tour = new ArrayList<>(list);
	}
	
	private ArrayList<Integer> randomPermutation(int n){
		ArrayList<Integer> templist = new ArrayList<Integer>();
		for(int i=0; i<n; i++){
			templist.add(i);
		}
		ArrayList<Integer> tour = new ArrayList<>(templist.size());
		do{
			int randomNumber = CS2004.UI(0, templist.size()-1);
			tour.add(templist.get(randomNumber));
			templist.remove(randomNumber);
		}while(templist.size()!= 0);
		return tour;
	}
	
	public double SalesManFitness(double[][] distances){
		double fitness = 0;
		for (int i=0; i< tour.size()-1; i++){
			int a = tour.get(i);
			int b = tour.get(i+1);
			fitness = fitness + distances[a][b];
		}
		int endCity = tour.get(tour.size()-1);
		int startCity = tour.get(0);
		fitness = fitness + distances[endCity][startCity];
		return fitness;
	}
	
	public ArrayList<Integer> smallChange(){
		int randomNumber = 0;
		int randomNumber2 = 0;
		previous_tour = (ArrayList<Integer>)tour.clone();
		while(randomNumber == randomNumber2){
			randomNumber = CS2004.UI(0, tour.size()-1);
			randomNumber2 = CS2004.UI(0, tour.size()-1);
		}
		int temp = tour.get(randomNumber);
		int temp2 = tour.get(randomNumber2);
		tour.set(randomNumber, temp2);
		tour.set(randomNumber2, temp);
		return tour;
	}
	
	public int tourSize(){
		return tour.size();
	}
    public void revertSmallChange() {
		tour = (ArrayList<Integer>)previous_tour.clone();
    }
	
	public ArrayList<Integer> getTour(){
		return tour;
	}
	
	public void printArray(){
		for(int i=0; i<tour.size();i++){
			System.out.print(tour.get(i));
		}
	}
}



//Some useful code for the CS2004 (2018-2019) Travelling Salesman Worksheet
class TSP 
{
	//Print a 2D double array to the console Window
	static public void PrintArray(double x[][])
	{
		for(int i=0;i<x.length;++i)
		{
			for(int j=0;j<x[i].length;++j)
			{
				System.out.print(x[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	//This method reads in a text file and parses all of the numbers in it
	//This method is for reading in a square 2D numeric array from a text file
	//This code is not very good and can be improved!
	//But it should work!!!
	//'sep' is the separator between columns
	static public double[][] ReadArrayFile(String filename,String sep)
	{
		double res[][] = null;
		try
		{
			BufferedReader input = null;
			input = new BufferedReader(new FileReader(filename));
			String line = null;
			int ncol = 0;
			int nrow = 0;
			
			while ((line = input.readLine()) != null) 
			{
				++nrow;
				String[] columns = line.split(sep);
				ncol = Math.max(ncol,columns.length);
			}
			res = new double[nrow][ncol];
			input = new BufferedReader(new FileReader(filename));
			int i=0,j=0;
			while ((line = input.readLine()) != null) 
			{
				
				String[] columns = line.split(sep);
				for(j=0;j<columns.length;++j)
				{
					res[i][j] = Double.parseDouble(columns[j]);
				}
				++i;
			}
		}
		catch(Exception E)
		{
			System.out.println("+++ReadArrayFile: "+E.getMessage());
		}
	    return(res);
	}
	//This method reads in a text file and parses all of the numbers in it
	//This code is not very good and can be improved!
	//But it should work!!!
	//It takes in as input a string filename and returns an array list of Integers
	static public ArrayList<Integer> ReadIntegerFile(String filename)
	{
		ArrayList<Integer> res = new ArrayList<Integer>();
		Reader r;
		try
		{
			r = new BufferedReader(new FileReader(filename));
			StreamTokenizer stok = new StreamTokenizer(r);
			stok.parseNumbers();
			stok.nextToken();
			while (stok.ttype != StreamTokenizer.TT_EOF) 
			{
				if (stok.ttype == StreamTokenizer.TT_NUMBER)
				{
					res.add((int)(stok.nval));
				}
				stok.nextToken();
			}
		}
		catch(Exception E)
		{
			System.out.println("+++ReadIntegerFile: "+E.getMessage());
		}
	    return(res);
	}
}

//Some useful code that we will probably reuse in later laboratories...
class CS2004 
{
	//Shared random object
	static private Random rand;
	//Create a uniformly distributed random integer between aa and bb inclusive
	static public int UI(int aa,int bb)
	{
		int a = Math.min(aa,bb);
		int b = Math.max(aa,bb);
		if (rand == null) 
		{
			rand = new Random();
			rand.setSeed(System.nanoTime());
		}
		int d = b - a + 1;
		int x = rand.nextInt(d) + a;
		return(x);
	}
	//Create a uniformly distributed random double between a and b inclusive
	static public double UR(double a,double b)
	{
		if (rand == null) 
		{
			rand = new Random();
			rand.setSeed(System.nanoTime());
		}
		return((b-a)*rand.nextDouble()+a);
	}
	//This method reads in a text file and parses all of the numbers in it
	//This code is not very good and can be improved!
	//But it should work!!!
	//It takes in as input a string filename and returns an array list of Doubles
	static public ArrayList<Double> ReadNumberFile(String filename)
	{
		ArrayList<Double> res = new ArrayList<Double>();
		Reader r;
		try
		{
			r = new BufferedReader(new FileReader(filename));
			StreamTokenizer stok = new StreamTokenizer(r);
			stok.parseNumbers();
			stok.nextToken();
			while (stok.ttype != StreamTokenizer.TT_EOF) 
			{
				if (stok.ttype == StreamTokenizer.TT_NUMBER)
				{
					res.add(stok.nval);
				}
				stok.nextToken();
			}
		}
		catch(Exception E)
		{
			System.out.println("+++ReadFile: "+E.getMessage());
		}
	    return(res);
	}
}
