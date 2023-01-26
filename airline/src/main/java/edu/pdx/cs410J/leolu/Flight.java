/**
 * The {code Flight} class
 * @author Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */

package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.AbstractFlight;


public class Flight extends AbstractFlight {
  private int flightNumber;
  private String dep; //Departure Airport 3-letter Code
  private String arr; //Arrival Airport 3-letter Code
  private String depDate;
  private String depTime;
  private String arrDate;
  private String arrTime;
  private String error = "";


  /**
   * Default Constructor
   * Creates a new <code>Flight</code>
   *
   * */
  public Flight(){}

  /**
   * Constructs a new instance of <code>Flight</code>
   * with a given flight number, departure airport code, arrival airport code,
   * departure datetime, and arrival datetime
   * Flight Constructor that accepts flight details as arguments
   * Creates a new <code>Flight</code>
   * @param fN Flight Number
   * @param dep Departure Airport 3-letter code
   * @param dDate Flight departure Date
   * @param dTime Flight departure Time
   * @param arr Arrival Airport 3-letter code
   * @param aDate Flight arrival Date
   * @param aTime Flight arrival Time
   *
   *
   * */
  public Flight(String fN, String dep, String dDate, String dTime,  String arr, String aDate, String aTime) {
    /*Validation of String Args to not be null*/
    try{
      validateNotNull(fN);
      validateNotNull(dep);
      validateNotNull(dDate);
      validateNotNull(dTime);
      validateNotNull(arr);
      validateNotNull(aDate);
      validateNotNull(aTime);
      validateAirportCode(dep);
      validateAirportCode(arr);
      validateThenSetFlightNumber(fN);
      this.dep = dep.toUpperCase();//forces airline code to be upper case
      this.arr = arr.toUpperCase();//forces airline code to be upper case
    }catch(NullPointerException e){
      this.error = "null input";
      System.err.println("null input, check -README for input instructions");
    }catch(NumberFormatException e){
      this.error = "Flight Number must be greater than 0!";
      System.err.println("Flight Number must be greater than 0!");
    }catch(IllegalArgumentException e) {
      this.error = e.getMessage();
      System.err.println(e.getMessage());
    }

    /*Setting object variables to String args*/
    this.depDate = dDate;
    this.depTime = dTime;
    this.arrDate = aDate;
    this.arrTime = aTime;
  }

  /**
   * @return integer Flight Number
   */
  @Override
  public int getNumber() {
    return flightNumber;
  }

  /**
   * Ensures String can be parsed into an integer
   * Ensures that the integer is greater than zero
   * @param input - input flight number String from Constructor
   * @throws IllegalArgumentException if number<=0
   * @throws NumberFormatException if String cannot be parsed to int
   * */
  private void validateThenSetFlightNumber(String input){
    int temp = Integer.parseInt(input);
    if(temp<=0) throw new IllegalArgumentException("Flight Number must be greater than 0!");
    this.flightNumber = temp;
  }

  /**
   * Checks if object is null;
   * @param object
   * @throws NullPointerException() should object be null
   * */
  private static void validateNotNull(Object object){
    if(object == null)throw new NullPointerException();
  }
  /**
   * Ensures input is only of length 3
   * Ensures input is alphabetical
   * @param input - airport code String from Constructor
   * @throws IllegalArgumentException when invalid airport code is used
   * */
  private void validateAirportCode(String input){
    String exceptionMessage = "Airport Code must be a 3-letter alphabetical code";
    if(input.length()!=3)throw new IllegalArgumentException(exceptionMessage);
    if(!isAirportCodeAlphabetic(input)) throw new IllegalArgumentException(exceptionMessage);
  }
  /**
   * @param input - airport code String from Constructor
   * @return boolean true when airport code is alphabetic
   * */
  public boolean isAirportCodeAlphabetic(String input)
  {
    return input.matches("^[a-zA-Z]*$");
  }
  /**
   * @return String Departure Airport 3-letter code
   **/
  @Override
  public String getSource() {
    return dep;
    //
  }
  @Override
  public String getDepartureString() {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  /**
   * @return String Arrival Airport 3-letter code
   **/
  @Override
  public String getDestination() {
    return arr;
  }

  @Override
  public String getArrivalString() {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  public String getError(){
    return error;
  }
}
