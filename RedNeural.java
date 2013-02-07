/**
 * Proyect 1 - Part I
 * @author Grace Gimon
 * @author Christian Chomiak
 * @author Oriana Baldizan
 */

import java.util.Vector;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RedNeural 
{
		static double[][] trainData;
		static double[][] testData;
		static double[] weights;
		static double[] deltaW;
		static double[] normMatrix;
        static double minNorm[];
        static double maxNorm[];
    	Vector<Double> errors = new Vector<Double>();
		Vector<Double> testErrors = new Vector<Double>();


		int validTests = 0;
    	int invalidTests = 0;


		int maxIterations = 2000; //15;
		double k = 0; // Number of iterations
		double error = 0;
		double output = 0;
		double target = 0;
     
    /* Create training examples
     * type: 0: AND, 1 : OR, 2: XOR
     * returns a two-dimension array
     * with 'number' of examples and
     * 3 positions regarding the binary expression and
     * the target
     */
     public boolean[][] createExamples(int type, int number)
     {
       boolean[][] example = new boolean[number][3];
       boolean bit = false;
         switch(type)
         {
             case 0:
                 for (int i = 0; i < 2; i++)
                 {
                    example[i][0] = bit;
                    example[i][1] = !bit;
                    example[i][2] = false;
                    bit = !bit;
                 }
                 for (int i = 2; i < 4; i++)
                 {
                    example[i][0] = bit;
                    example[i][1] = bit;
                    example[i][2] = bit && bit; //true
                    bit = !bit;
                 }
                 System.out.println((bit && !bit) + " " + example[0][2]);
                 break;
             case 1: 
                  for (int i= 0; i<2; i++){
                    example[i][0] = bit;
                    example[i][1] = !bit;
                    example[i][2] = (bit || !bit);
                    bit = !bit;
                 }
                 for (int i= 2; i<4; i++){
                    example[i][0] = bit;
                    example[i][1] = bit;
                    example[i][2] = (bit || bit);
                    bit = !bit;
                 }               
                 break;
             case 2:
                    for (int i= 0; i<2; i++){
                    example[i][0] = bit;
                    example[i][1] = !bit;
                    example[i][2] = true;
                    bit = !bit;
                 }
                 for (int i= 2; i<4; i++){
                    example[i][0] = bit;
                    example[i][1] = bit;
                    example[i][2] = false;
                    bit = !bit;
                 } 
                 break;
         }
         
       for (int i = 0; i < number; i++)
            for (int j = 0; j < 3; j++)
               System.out.println("example[" + i + "][" + j + "] = " + example[i][j]);

         return example;
     }
     
	public double toDoub(boolean b)
	{
		if (b) 
			return 1;
		else 
			return  0;         
	}

	public double toDoubTarg(boolean b)
	{
		if (b) 
			return 1;
		else 
			return  -1;         
	}
     
	public double calcOutputBool(double[] weight, boolean[] example)
	{
		double output = weight[0] * toDoub(example[0]);
		output += weight[1] * toDoub(example[1]);
		output += weight[2]; //W0

		if (output > 0)
			return 1;
		else 
			return -1; 
	}


	// Calculates the output of the neuron
	public double calcOutput(double[] example)
	{
		double output = 0;

		for(int i =0; i < weights.length-1; i++)
		{
			output += weights[i] * example[i]; 
		} 
		output += weights[weights.length-1]; //W0

		if (output > 0)
			return 1;
		else 
			return -1; 
	}


	// Perceptron Neuron
	void PLR(double rate)
	{
		int numberOfWeights = weights.length;         

		// Random initialization of weights
		for (int j = 0; j < numberOfWeights; j++)
		{
			weights[j] = Math.random()/10;
		}
       
		do
		{
 			error = 0;

			// For each example
 			for (int i = 0; i < trainData.length; i++)
 			{		
 				output = calcOutput(trainData[i]);
 				target = trainData[i][numberOfWeights-1];

				error += Math.pow( (target - output), 2);

				for (int j = 0; j < numberOfWeights-1; j++) 
				{
					deltaW[j] = rate * ( target - output) * trainData[i][j];
					weights[j] += deltaW[j];
				}
				deltaW[numberOfWeights-1] = rate * (target - output);
				weights[numberOfWeights-1] += deltaW[numberOfWeights-1];
				//if ( ((double) Math.round(target) - output) == 0 )
			//	System.out.println("Example " + i + " target /  output " + (double) Math.round(target) +" " +output);  				      
			}

			error /= trainData.length;			
			errors.add(error);
			k++;
		}
		while (error != 0 && k < maxIterations);

		System.out.println("Number of iterations: " +k);
	}


	// Delta Learning Rule
	// Batch Mode
	void DLRB(double rate)
	{       
		int numberOfWeights = weights.length;

		// Random initialization of weights
		for (int j = 0; j < numberOfWeights; j++)
		{
			weights[j] = Math.random()/10;
		}

		do{
       
			// Initialize DeltaW to zero
			for (int j = 0; j< numberOfWeights; j++){
				deltaW[j] = 0;
			}
			error = 0;

			//For each example
			for (int i = 0; i< trainData.length; i++){
				output = (calcOutput(trainData[i])); //Compute the output
				target = trainData[i][numberOfWeights-1];
				error += Math.pow( (target - output), 2);
          
				//For each unit weight, do
				for (int j = 0; j < numberOfWeights -1; j++){
					deltaW[j] += (target - output) * trainData[i][j]; 
				}
				deltaW[numberOfWeights-1] +=  (target - output);
			}

			//Then, update each weight
			for (int i = 0; i< numberOfWeights - 1; i++){
				weights[i] += rate * deltaW[i];
			}
			weights[numberOfWeights-1] += rate * deltaW[numberOfWeights-1];

			// Verifying stop criterion          
			error /= trainData.length; 
			errors.add(error);
			k++;
		} while(error != 0 && k < maxIterations);

		System.out.println("Number of iterations: " +k);
	}



	//Delta Learning Rule
	//Incremental Mode
	void DLRI(double n){
		PLR(n);
	}



	// ADALINE
	void ADALINE(double[][] example, double n)
	{
		int numberOfWeights = weights.length;

		// Random initialization of weights
		for (int j = 0; j < numberOfWeights; j++)
		{
			weights[j] = Math.random()/10;
		}

		do{
			error = 0;
			for (int j = 0; j < numberOfWeights; j++)
			{       
				deltaW[j] = 0;
			}

			// For each example
			for (int i= 0; i<example.length; i++)
			{
				output = calcOutput(example[i]);
				target = example[i][numberOfWeights - 1];
				error += Math.pow((target - output),2);

				for (int j = 0; j<numberOfWeights - 1; j++){
					deltaW[j] += n * (target - output)* example[i][j]; 
				}
				deltaW[numberOfWeights-1] +=   n * (target - output);
				//if ( ((double) Math.round(target) - output) == 0 )
				//System.out.println("Example " + i+ " output " + output);
			}

			//Then, update each weight
			for (int j = 0; j<numberOfWeights-1; j++) {
				weights[j] += deltaW[j]; 
        		weights[numberOfWeights-1] += deltaW[numberOfWeights-1];
			}

			k++;
			error /= example.length;
			errors.add(error);

		}while(error != 0 && k < maxIterations);
		
		System.out.println("Number of iterations: " +k);
	}

	void ADALINE_Test(double[][] example)
	{
		int numberOfWeights = weights.length;

		error = 0;

		// For each example
		for (int i = 0; i < example.length; i++)
		{
			output = calcOutput(example[i]);
			target = example[i][numberOfWeights - 1];
			error += Math.pow((target - output), 2);

			if (target == output)
				validTests++;
			else
				invalidTests++;
		}

		k++;
		error /= example.length;
		testErrors.add(error);
		
		System.out.println("Number of iterations: " + k);
	}

	// Normalize training data
	private static void normalizeTrainingData()
	{
		double aux;

		// Normalize the data 
		for (int i=0; i < trainData.length; i++) 
		{
			for (int j=0; j < weights.length; j++)
			{
				if (j < weights.length-1)
                {
                    if (maxNorm[j] == minNorm[j])
                        aux = trainData[i][j];
                    else                    
                        aux = (trainData[i][j] - minNorm[j]) / (maxNorm[j] - minNorm[j]);
					trainData[i][j] = (double) Math.round(aux);
                }
			//	System.out.println ("Normalizado " + i + " " + j + " " + trainData[i][j]);
			}
		}
	}

	// Normalize tes tdata
	private static void normalizeTestData()
	{
		double aux;

		// Normalize the data 
		for (int i=0; i < testData.length; i++) 
		{
			for (int j=0; j < weights.length; j++)
			{ 
				if (j < weights.length-1)
                {
                    if (maxNorm[j] == minNorm[j])
                        aux = testData[i][j];
                    else                    
                        aux = (testData[i][j] - minNorm[j]) / (maxNorm[j] - minNorm[j]);
					testData[i][j] = (double) Math.round(aux);
                }
			//	System.out.println ("Normalizado " + i + " " + j + " " + trainData[i][j]);
			}
		}
	}

	// Saves training data in a global array
	private static void readTestData(String trainF) 
	{
		try {
			BufferedReader br_train = new BufferedReader(new FileReader(trainF));
			String str;
			int numberOfExamples, numberOfWeights, i;

			// Reads first line with the info
			str = br_train.readLine();
			String[] strArr = str.split(" ");
			numberOfExamples = Integer.parseInt(strArr[0]);
			numberOfWeights = Integer.parseInt(strArr[1]);

			// Initializes the Examples and Weights arrays
			testData = new double[numberOfExamples][numberOfWeights];
         
			i = 0;

			// Reads examples
			while ( (str = br_train.readLine()) != null )
			{
				strArr = str.split(",");
				for (int j=0; j < strArr.length; j++)
				{
					testData[i][j] = Double.parseDouble(strArr[j]);                      
				}

				i++;
			}

	
			br_train.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	// Saves training data in a global array
	private static void readTrainingData(String trainF) 
	{
		try {
			BufferedReader br_train = new BufferedReader(new FileReader(trainF));
			String str;
			int numberOfExamples, numberOfWeights, i;

			// Reads first line with the info
			str = br_train.readLine();
			String[] strArr = str.split(" ");
			numberOfExamples = Integer.parseInt(strArr[0]);
			numberOfWeights = Integer.parseInt(strArr[1]);

			// Initializes the Examples and Weights arrays
			trainData = new double[numberOfExamples][numberOfWeights];
			weights = new double[numberOfWeights];
			deltaW = new double[numberOfWeights];
			normMatrix = new double[numberOfWeights];

            minNorm = new double[normMatrix.length - 1];
            maxNorm = new double[normMatrix.length - 1];            
			
			i = 0;

			//Read Min and Max Values
			for (int p = 0; p < minNorm.length; p++)
			{
				str = br_train.readLine();
				if (str == null)
					System.exit(1);

				strArr = str.split(" ");
				minNorm[p] = Double.parseDouble(strArr[0]);
				maxNorm[p] = Double.parseDouble(strArr[1]);
				System.out.println("Min[" + p + "] = " + minNorm[p] + " --- Max[" + p + "] = " + maxNorm[p]);
			
			}

			// Reads examples
			while ( (str = br_train.readLine()) != null )
			{
				strArr = str.split(",");
				for (int j=0; j < strArr.length; j++)
				{
					trainData[i][j] = Double.parseDouble(strArr[j]);                      
				}

				i++;
			}

	
			br_train.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
   
    /**
     * @param args[0] : Number of the algorithm to use
     *					1: Perceptron
     *					2: Delta Learning Rule Batch
     *					3: Delta Learning Rule Increm
     *					4: Adaline
     * @param args[1] : Learning rate
     * @param args[2] : Training data file
     * @param args[3] : Testing data file
     */
	public static void main(String[] args) {
		
		int option = 0;
		RedNeural rn = new RedNeural();

		Vector<Double> rates = new Vector<Double>();
		 rates.add(0.05);
		 rates.add(0.1);
		 rates.add(0.2);
		 rates.add(0.3);
		 rates.add(0.4);
		 rates.add(0.5);
		 rates.add(0.99);
		 rates.add(0.01);
		 
		String[] ratesNames = new String[rates.size()];
		for (int i = 0; i < rates.size(); i++)
			ratesNames[i] = "" + rates.get(i);

		double[][] errorCharts;
      
		errorCharts = new double[rates.size()][rn.maxIterations];

		System.out.println("Rates size: " + rates.size() + "\n");

		if (args.length > 0) 
		{			
			if (args.length >= 4) 
			{
				option = Integer.parseInt(args[0]);
				readTrainingData(args[2]);
				readTestData(args[3]);


				if (option == 4)
				{
					normalizeTrainingData();
					normalizeTestData();		
				}

				for (int i = 0 ; i < rates.size(); i++)
				{
					rn.k = 0;

					if (option == 1){ // Perceptron
						rn.PLR(rates.get(i));
					}
					else if (option == 2) // Delta Rule Batch Mode
						rn.DLRB(rates.get(i));			

					else if (option == 3) // Delta Rule Incremental Mode
						rn.DLRI(rates.get(i));

					else if (option == 4) // ADALINE
					{	
						rn.ADALINE(trainData, rates.get(i));
						if (i == rates.size() - 1)
						{
							rn.ADALINE_Test(testData);
							System.out.println("Valids: " + rn.validTests + " - Invalids: " + rn.invalidTests);
						}
					}

			     	for (int j = 0; j < rn.errors.size() ; j++)
			     	{
			       		errorCharts[i][j] = rn.errors.get(j);
			       	}

			       	rn.errors = new Vector<Double>(); //.clear();


				}
			} 
			else {
				System.out.println("Please indicate the option, the learning rate and files of the train and test examples.");
				System.exit(-1);
			}


			// Prints the chart for the Error
			System.out.println("Printing Chart of the Error");
     	XYChart xychart = new XYChart();

      
     	xychart.getChart(errorCharts, ratesNames, option);
      
		} else {
			System.out.println("Please indicate the algorithm to evaluate");
		}
	}
}
