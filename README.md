# affirm


## Prerequisites to run the code

- Maven 3.8.4
- JDK 8+

## How to test
- Build: `mvn clean package`
- Run: `java -jar target/Affirm-1.0-SNAPSHOT.jar <folder_name>`, for example `java -jar target/Affirm-1.0-SNAPSHOT.jar data/small` 

## Explanation

#### How long did you spend working on the problem? What did you find to be the most difficult part?

**Ans**. Around 8-9 hours on this assignment and most time spent was on optimizing it for modularity and extensibility. I found the part where I had to sort out the logic of creating Covenant as the data in the input table is not normalized. Making Covenant extensible was turning out to be an over engineering effort, so at last I simplified it in a way the existing code can be modified to add complex logic in future if needed. The current design of the solution has been partially implemented with the design, where once a loan is evaluated and assigned to a facility, it generates an event. And the event subscriber can act on it in a real time or batch mode as needed. For now the event listener is collecting Loan Assignment in a list and immediately calling calculate Yield and keep data for Reporting.

#### How would you modify your data model or code to account for an eventual introduction of new, as-of-yet unknown types of covenants, beyond just maximum default likelihood and state restrictions? 

**Ans**. The `Covanent` class is implemented where each Covenant type is represented on itrs own hashMap, there are two ways we can extend the model. Not so ideal, we can add more to enum and keep extending the Covenant Type; or a more scalable solution would be to create subclasses for each Covenant and then have them represented as one set of objects and each one can have their own behavior.

#### How would you architect your solution as a production service wherein new facilities can be introduced at arbitrary points in time. Assume these facilities become available by the finance team emailing your team and describing the addition with a new set of CSVs. 

**Ans**. Application has been designed with extensibility in mind, “core” loan processing logic is independent of the data source(repository of input). In the repository package, we have an interface which for now has been implemented to read one the CSV file. It can be modified to fetch data from any different source, or mix of source, or set of csv files or listen to any change on the file system and pick a set of files from a directory. As of now, we load all facilities and process loans, if the frequency of file changes becomes frequent and number of facilities increases, we may decide to introduce caching of facilities and make facilities loading an event listener based service.

#### Your solution most likely simulates the streaming process by directly calling a method in your code to process the loans inside of a for loop. What would a REST API look like for this same service? Stakeholders using the API will need, at a minimum, to be able to request a loan be assigned to a facility, and read the funding status of a loan, as well as query the capacities remaining in facilities. 

**Ans**. The current design is implemented in a way that data input and processing are completely independent, the loan processing logic is contained in 'core' module / package. At any point we can expose it as either batch code, Web Service or REST API. I would make further enhancement before exposing current logic as a REST API. Introduce a concept of transaction id or trace id, which will help maintain the auditable of requests / responses and data in a system easy to manage and audit when needed. I move that id to the header so that I don't have to maintain / modify each service data level. 


POST api/v1/Loan
  -d {
    interestRate: <float>
    amount: <int>
    defaultLikelyHood: <float>
    state: <str>
  }
  - GOOD: returns 200, facility id that the loan is assigned to 
		200, {
	                facility_id : ""
	                loan_id: ""
	                interestRate: ""
	             }
  - ERR: returns 40X, if loan request not fulfillable
			404 , {message: "Out of capacity"}
			404 , {message: "Does not satisfy covenant <cov details>"}
			422 , {message: "malformed json"}
			401 , {message: "unauthorized"}

  GET api/v1/loans/{loans_id}
  - GOOD: returns 200, {
	                facility_id : ""
	                loan_id: ""
	                interestRate: ""
	             }
	
  - ERR: returns 404, loan does not exist
                     404 , {message: “loan does not exist”}

  GET api/v1//facilities/{facility_id}
  - GOOD: returns 200, returns details of facility
		     {
	                facility_id : ""
	                interestRate: ""
	                amountInCents: ""
	                calculatedYield: ""
	             }
  - ERR: returns 404, facility does not exist
	
#### How might you improve your assignment algorithm if you were permitted to assign loans in batch rather than streaming? We are not looking for code here, but pseudo code or description of a revised algorithm appreciated.

**Ans**. The current code assigns best facility to a given loan, as it is looking to optimize the loan. But for the overall system, that is not the best option as certain loans can miss the best opportunity due to Yield constraints. So there can be many different ways we can look. Possibly we can find the most optimal loan for a given facility from a batch of loans. Another one which would be computation heavy where we create various combinations of loan and facility combinations and assign each of them a profitability score and then assign them in order of score. Some of these approaches may vary based on what is our primary objective for processing loans in batch, speed, profitability or optimizing facilities as best etc.
	
####  Discuss your solution’s runtime complexity.

**Ans**

Time Complexity:- 
	
* F = # items in facilities.csv - I am assuming facilities are a bounded number, so it can be considered as constant. 
* C = # items in covenants.csv - I am assuming covenants are a bounded number, so it can be considered as constant.
* L = # items in load.csv - Loan can scale as company grows and hence loan is a unbounded 
	
The core logic of code 

	* Loads data from csv file - Constant
	* Creates a covenants object by iterating through each entry and breaking it down by location & interest (normalizing the denormalized entry)  - constant
	* Iterates each loan against each facility to determine best fit facility O(f(loan * facility)) -> facility being constant it may be considered as O(loan) best case.

	
Space Complexity

* Everything is loaded into memory. The model to process the loans has space O(c+f+b), where c = # normalized covenants, f= # facilities, b= # banks, l=#loans	
