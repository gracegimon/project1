/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package redneural;

import java.util.Vector;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Gimbet
 */
public class RedNeural 
{
		static double[][] trainData;
		static double[][] testData;
		static double[] weights;
		static double[] deltaW;
		static double[] normMatrix;

    double bias = 0.1;          
    int numIterations = 30;
    Vector<Double> errors = new Vector<Double>();// new double[numIterations]; 

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
     
     public double calcOutput(double[] weight, boolean[] example)
     {
         double output = weight[0] * toDoub(example[0]);
         output += weight[1] * toDoub(example[1]);
         output += weight[2]; //W0
         if (output > 0)
             return 1;
         else 
             return -1; 
     }

	public double calcOutputAdaline(double[] weight, double[] example)
	{
		double output = 0;

		for(int i =0; i < weight.length-1; i++)
		{
			output += weight[i] * example[i]; 
		} 
		output += weight[weight.length-1]; //W0
		if (output > 0)
			return 1;
		else 
			return -1; 
	}


			// Perceptron
      void PLR(boolean[][] example, double n)
      {
      	int numberOfWeights = example[0].length;
        double[] weights = new double[numberOfWeights];
       	double[] deltaW = new double[numberOfWeights];
				double b = 1.0;
				double k = 0; // Number of iterations
          

       	for (int j = 0; j < numberOfWeights; j++)
        {
        	weights[j] = Math.random()/10;
          System.out.println("WEIGHTS "+ weights[j]);
        }
        double error = 0;
        do
        {
          error = 0;
        	for (int i = 0; i < example.length; i++)
          {		
          	double output = calcOutput(weights, example[i]);
            double target = toDoubTarg(example[i][2]);
            error += Math.pow( (target - output), 2);
      /*      System.out.println(" Ejemplo "+ i + "\n");
            System.out.println("Output : "+ output + "\n");
            System.out.println("Target : "+(example[i][2]) );
            System.out.println("Target - Output "+ (target - output) );						
            System.out.println("Error Cuadratico : "+ error);
            System.out.println ("Peso 1 " +weights[0]+ "  Peso 2 "+weights[1] + " Peso 3 " +weights[2]+  "\n");
						*/
            for (int j = 0; j < numberOfWeights -1; j++) 
           	{
            	deltaW[j] = n * ( target - output) * toDoub(example[i][j]);
             	weights[j] += deltaW[j];
            //  System.out.println("delta: " + deltaW[j]);
           	}
            deltaW[2] = n * (target - output);
            weights[2] += deltaW[2];  				
					                
          }
          error /= example.length;

          k++;
          errors.add(error);  
          System.out.println("\n");
        }
        while (error != 0 && k < 100);
         System.out.println("Iteration k: " + k);
      }


      // Delta Learning Rule
      // Batch Mode
      void DLRB(boolean[][] example, double n)
      {       
		int numberOfWeights = example[0].length;
		double[] weights = new double[numberOfWeights];
		double[] deltaW = new double[numberOfWeights];
        for (int j = 0; j < numberOfWeights; j++)
        {
          weights[j] = 0;
          deltaW[j] = 0;
        }

        do{
        
          // Initialize DeltaW to zero
          for (int j = 0; j< numberOfWeights; j++){
            deltaW[j] = 0;
          }
          error = 0;
          //For each example
          for (int i = 0; i< example.length; i++){
            //Compute the output
            output = (calcOutput(weights, example[i]));
            target = toDoubTarg(example[i][2]);
            error += Math.pow( (target - output), 2);
            
            //For each unit weight, do
            for (int j = 0; j < numberOfWeights -1; j++){
              deltaW[j] += (target - output) * toDoub(example[i][j]); 
            }
            deltaW[2] +=  (target - output);
          }

          //Then, update each weight
          for (int i = 0; i< numberOfWeights - 1; i++){
            weights[i] += n * deltaW[i];
          }
            weights[2] += n * deltaW[2];

          // Verifying stop criterion          

          error /= example.length; 
          errors.add(error);
          k++;
        } while(error != 0 && k < 10);
        System.out.println ("Peso 1 " +weights[0]+ "  Peso 2 "+weights[1] + " Peso 3 " +weights[2]+  "\n");
      }

      //Delta Learning Rule
      //Incremental Mode
      void DLRI(boolean[][] example, double n){
        PLR(example,n);
      }

	// ADALINE
	void ADALINE(double[][] example, double n)
	{
		int numberOfWeights = weights.length;

		for (int j = 0; j < numberOfWeights; j++)
		{
			weights[j] = 0;
			deltaW[j] = 0;
		}

		do{
			error = 0;
			for (int j = 0; j < numberOfWeights; j++)
			{       
				deltaW[j] = 0;
			}

			for (int i= 0; i<example.length; i++)
			{
				double output = calcOutputAdaline(weights,example[i]);
				double target = example[i][numberOfWeights-1];
				//System.out.println("Para " + i + " output " + output);

				error += Math.pow((target - output),2);
				for (int j = 0; j<numberOfWeights-1; j++){
					deltaW[j] += n * (target - output)* example[i][j]; 
				}
				deltaW[numberOfWeights-1] +=   n * (target - output);
			}

			for (int j = 0; j<numberOfWeights-1; j++)
				weights[j] += deltaW[j]; 
        
			weights[numberOfWeights-1] += deltaW[numberOfWeights-1];
			k++;
			error /= example.length;
			errors.add(error);
			System.out.println("Iteration " + k + " Error " + error);

		}while(error != 0 && k < 1000);
		
	}

	//Truncate
	private static double truncate(double x){
  	if ( x > 0 )
  	  return Math.floor(x * 100)/100;
  	else
  	  return Math.ceil(x * 100)/100;
	}

	// Normalize training data
	private static void normalizeTrainingData()
	{
		double aux;

		// Calculates the average for each attribute
		for (int i=0; i < normMatrix.length; i++)
		{
			normMatrix[i] = normMatrix[i] / trainData.length;
		}

		// Normalize the data 
		for (int i=0; i < trainData.length; i++) 
		{
			for (int j=0; j < weights.length-1; j++)
			{
				aux = trainData[i][j] / normMatrix[j]; 
				trainData[i][j] = (double) Math.round(aux);
				//System.out.println ("Normalizado " + i + " " + j + " " + trainData[i][j]);
			}
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

			// Reads examples
			i = 0;
			while ( (str = br_train.readLine()) != null )
			{
				strArr = str.split(",");
				for (int j=0; j < strArr.length; j++)
				{
					trainData[i][j] = Double.parseDouble(strArr[j]);

					if (j < strArr.length-1) // Last value is normalized
						normMatrix[j] += Double.parseDouble(strArr[j]);
				}

				i++;
			}
	
			br_train.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
   
    /**
     * @param args the command line arguments
     */
	public static void main(String[] args) {
		
		int option;
		RedNeural rn = new RedNeural();

		if (args.length > 0) {

 	    option = Integer.parseInt(args[0]);
				
			if (option == 1) // Perceptron
				rn.PLR(rn.createExamples(0, 4), 0.1);

			else if (option == 2) // Delta Rule Batch Mode
				rn.DLRB(rn.createExamples(0, 4), 0.1);					
					
			else if (option == 3) // Delta Rule Incremental Mode
				rn.DLRI(rn.createExamples(0, 4), 0.1);

			else if (option == 4) // ADALINE
			{
				if (args.length >= 3 ) {
					//BufferedReader br_test = new BufferedReader(new FileReader(args[2]));

					readTrainingData(args[1]);		
					normalizeTrainingData();		
					rn.ADALINE(trainData, 0.1);
				}
				else {
					System.out.println("Please indicate the files of the train and test examples.");
					System.exit(-1);
				}
			}


			// Prints the chart for the Error
			System.out.println("Printing chart");
     	XYChart xychart = new XYChart();
      
     	double[] elCosoEste = new double[rn.errors.size()];
      
     	for (int i = 0; i < rn.errors.size() ; i++)
       	elCosoEste[i] = rn.errors.get(i);
      
     	xychart.getChart(elCosoEste);
      
		} else {
			System.out.println("Please indicate de option to evaluate");
		}
	}
}
