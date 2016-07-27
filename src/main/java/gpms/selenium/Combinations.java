package gpms.selenium;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Combinations {
	
	/*
	 * Array of strings could still work, BUT it needs to be strings for the IDs of 
	 * all my drop down menus
	 * Note: expand all button does exist, but is not displayed
	 */
    public static void main(String[] args){
    	/*
    	 * Get ID of each accordion on css page
    	 */

    	String[] arr = {"A","B","C","D","E","F"};
        for (int ii = 0; ii <= arr.length; ii++)
        {
            combinations2(arr, ii, 0, new String[ii]);

        }
//        combinations2(driver, arr, 3, 0, new String[3]);
    }
   
	 public static void combinations2(String[] arr, int len, 
			 int startPosition, String[] result)
	 {
		 if (len == 0)
	     {
	        System.out.println(Arrays.toString(result));
	        return;
	     }       
	     for (int ii = startPosition; ii <= arr.length-len; ii++)
	     {
	        result[result.length - len] = arr[ii];
	        combinations2(arr, len-1, ii+1, result);
	     }
	}

}
