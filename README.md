# affirm


##Prerequisites to run the code

- Maven 3.8.4
- JDK 8+

##How to test
- Build: `mvn clean package`
- Run: `java -jar target/Affirm-1.0-SNAPSHOT.jar <folder_name>`, for example `java -jar target/Affirm-1.0-SNAPSHOT.jar data/small` 

##Explanation
1. How long did you spend working on the problem? What did you find to be the most difficult part?

**Ans**. Around 5-6 hours on this assignment and most time spent were on making it modular and extensible. I found the part where I had to sort out the logic of create CompositeCovenant and then break it down into individual Covenant into max_default_likelihood or banned_state (bank based). I had to perform a proprocessing of the covenant data to achieve that feature.

2. How would you modify your data model or code to account for an eventual introduction of new, as-of-yet unknown types of covenants, beyond just maximum default likelihood and state restrictions? 

**Ans**. The `Covanent` class is extensible and new variants can be added by imlementing the interface, A loan and be filtered through a set of `Covanent`'s by a facility.

3. How would you architect your solution as a production service wherein new facilities can be introduced at arbitrary points in time. Assume these facilities become available by the finance team emailing your team and describing the addition with a new set of CSVs. 

**Ans**. My application has been design with modular in mind, “core” module is independent of the data source(repository of input). In the repository package, we have interface which for now has been implemented to read the CSV file. It can be extended to fetch data from any different source, or mix of source, or set of csv files.


4. Your solution most likely simulates the streaming process by directly calling a method in your code to process the loans inside of a for loop. What would a REST API look like for this same service? Stakeholders using the API will need, at a minimum, to be able to request a loan be assigned to a facility, and read the funding status of a loan, as well as query the capacities remaining in facilities. 

**Ans**. Currently we have done soem prep work we have class LoanAssignmentHandler with two public methods assignLoan and getYieldByFacilityId which can be wrapped and enhanced as endpoints for consumption. 

POST api/v1/Loan
  -d {
    interest_rate: <float>
    amount: <int>
    default_likelihood: <float>
    state: <str>
    request_facility_id: <int>
  }
  - GOOD: returns 200, facility id that the loan is assigned to 
		200, {"facility_id": “”}
  - ERR: returns 40X, if loan request not fulfillable
			404 , {message: "Out of capacity"}
			404 , {message: "Does not satisfy covenant <cov details>"}
			422 , {message: "malformed json"}
			401 , {message: "unauthorized"}

  GET api/v1/loans/{loans_id}
  - GOOD: returns 200, funding status of the loan
	
  - ERR: returns 404, loan does not exist
                     404 , {message: “loan does not exist”}

  GET api/v1//facilities/{facility_id}
  - GOOD: returns 200, returns funding capacity in facility
  - ERR: returns 404, facility does not exist

  Optional:

  POST api/v1//covenants/
  -d {
    facility_id <int>
    max_default_likelihood <float>
    banned_state: <str>
  }

  POST api/v1//facilities/
  -d {
    amount: <int>
    interest_rate: <float>
    bank_id <int>
  }

The loan service gets all facilities using the GET endpoint, calculates the amount remaining in the facility
and updates the facility (based on facility id) with the new amount and new yield

The POST is used to create a new Facility

The funding status of a loan can be queried using - GET /api/v1/loan/<loan_id>/
To get the information about a particular facility, query GET /api/v1/facilities/<facility_id>
