package patientManagement;

/**
 *  Class for Addresses
 * simplifies the process of writing addresses in AddressDt style
 * minimum arguments are: use, line, zip, city, country
 * @author Anne
 *
 */
public class MyAddress {
    
    private int use;
    private String line;
    private String zip;
    private String city;
    private String country;
    
 
    /**
     * Creates an address
     * @param use: (1-4)
     * @param line: street and number
     * @param zip
     * @param city
     * @param country
     */
    public MyAddress(int use, 
	    		String line, 
	    		String zip, 
	    		String city, 
	    		String country) {
	
	this.use = use;
	this.line = line;
	this.zip = zip;
	this.city = city;
	this.country = country;
    }
    
    //getter:
    public String getCity() {
	return city;
    }
    public String getCountry() {
	return country;
    }
    public String getLine() {
	return line;
    }
    public int getUse() {
	return use;
    }
    public String getZip() {
	return zip;
    }
    
}
