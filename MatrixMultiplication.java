package ca.senecacollege.MatrixMultiplication;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;

public class MatrixMultiplication {
	public static void main(String...args) throws InterruptedException {
		
		//size of array
		final int SIZE = 2000;
		final int SMALL_ARRAY_SIZE = 500;
		
		//two matrix
		double[][] mat1 = new double[SIZE][SIZE];
		double[][] mat2 = new double[SIZE][SIZE];
		
		double[][] addition = new double[SIZE][SIZE];
		
		//populate the matrix with random numbers
		for(int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				mat1[i][j] = random();
				mat2[i][j] = random();
			}
		}
		
		//divide each matrix into four pieces to save time
		double[][] mat11 = new double[SMALL_ARRAY_SIZE][SMALL_ARRAY_SIZE];
		double[][] mat12 = new double[SMALL_ARRAY_SIZE][SMALL_ARRAY_SIZE];
		double[][] mat13 = new double[SMALL_ARRAY_SIZE][SMALL_ARRAY_SIZE];
		double[][] mat14 = new double[SMALL_ARRAY_SIZE][SMALL_ARRAY_SIZE];
		
		double[][] mat21 = new double[SMALL_ARRAY_SIZE][SMALL_ARRAY_SIZE];
		double[][] mat22 = new double[SMALL_ARRAY_SIZE][SMALL_ARRAY_SIZE];
		double[][] mat23 = new double[SMALL_ARRAY_SIZE][SMALL_ARRAY_SIZE];
		double[][] mat24 = new double[SMALL_ARRAY_SIZE][SMALL_ARRAY_SIZE];
		
		//result of upper arrays
		double[][] add11 = new double[SMALL_ARRAY_SIZE][SMALL_ARRAY_SIZE];
		double[][] add22 = new double[SMALL_ARRAY_SIZE][SMALL_ARRAY_SIZE];
		double[][] add33 = new double[SMALL_ARRAY_SIZE][SMALL_ARRAY_SIZE];
		double[][] add44 = new double[SMALL_ARRAY_SIZE][SMALL_ARRAY_SIZE];
		
		for(int i = 0, sec1 = 500, third1 = 1000, fourth1 = 1500; i < 500; i++, sec1++, third1++, fourth1++) {
			for(int j = 0, sec2 = 500, third2 = 1000, fourth2 = 1500; j < 500; j++, sec2++, third2++, fourth2++) {
				//first 500 * 500 elements
				mat11[i][j] = mat1[i][j];
				mat21[i][j] = mat2[i][j];
				
				//second 500 elements
				mat12[i][j] = mat1[sec1][sec2];
				mat22[i][j] = mat2[sec1][sec2];
				
				//third 500 elements
				mat13[i][j] = mat1[third1][third2];
				mat23[i][j] = mat2[third1][third2];
				
				//fourth 500 elements
				mat14[i][j] = mat1[fourth1][fourth2];
				mat24[i][j] = mat2[fourth1][fourth2];
			}
		}	//end of for loop
		
		Runnable addMat1 = new MatrixAddition(mat1, mat2, addition);
		Thread t1 = new Thread(addMat1);
		Instant start = Instant.now();
		t1.start();
		t1.join();
		Instant end = Instant.now();
		long timeElapsed = Duration.between(start, end).toMillis();
		
		System.out.println("Time taken by sequential method is: " + timeElapsed + " Milli Seconds");
		
		//time taken by parallel method
		Runnable addMat2 = new MatrixAddition(mat11, mat21, add11);
		Runnable addMat3 = new MatrixAddition(mat12, mat22, add22);
		Runnable addMat4 = new MatrixAddition(mat13, mat23, add33);
		Runnable addMat5 = new MatrixAddition(mat14, mat24, add44);
		Thread t2 = new Thread(addMat2);
		Thread t3 = new Thread(addMat3);
		Thread t4 = new Thread(addMat4);
		Thread t5 = new Thread(addMat5);
		start = Instant.now();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t2.join();
		t3.join();
		t4.join();
		t5.join();
		for(int i = 0, sec1 = 500, third1 = 1000, fourth1 = 1500; i < 500; i++, sec1++, third1++, fourth1++) {
			for(int j = 0, sec2 = 500, third2 = 1000, fourth2 = 1500; j < 500; j++, sec2++, third2++, fourth2++) {
				addition[i][j] = add11[i][j];
				addition[sec1][sec2] = add22[i][j];
				addition[third1][third2] = add33[i][j];
				addition[fourth1][fourth2] = add44[i][j];
			}
		}
		end = Instant.now();
		timeElapsed = Duration.between(start, end).toMillis();
		
		System.out.println("Time taken by parallel method is: " + timeElapsed + " Milli Seconds");
		
		
	}
	
	//return a two digit random double
	public static double random() {
		double val = (1 + (Math.random() * 100));
		return BigDecimal.valueOf(val).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
	
}
