import java.io.*;
import java.text.*;
import java.util.*;

class Policy implements Serializable {
	
	private int policyID;
	private int policyholderID;
	private String policyDes;
	private int type;

	private int term;
	private Calendar startDate;
	private Calendar expiryDate = new GregorianCalendar();
	
	private int premium;
	private int paid;
	private ArrayList<Calendar> payments = new ArrayList<Calendar>();
	private boolean[] paymentStatus;
	
	private int claimsPaid;
	private ArrayList<Calendar> claimDate = new ArrayList<Calendar>();
	private ArrayList<Integer> claimPaid = new ArrayList<Integer>();
	private ArrayList<String> claimDes = new ArrayList<String>();

	public Policy(int policyID, int policyholderID, String policyDes, Calendar startDate, int term,int premium, int paid, int type)
	{
		this.policyID = policyID;
		this.policyholderID = policyholderID;
		this.policyDes = policyDes;
		this.term = term;
		this.startDate = startDate;
		expiryDate.set( Calendar.YEAR, term + startDate.get(Calendar.YEAR) );
		expiryDate.set( Calendar.MONTH, startDate.get(Calendar.MONTH) );
		expiryDate.set( Calendar.DAY_OF_MONTH, startDate.get(Calendar.DAY_OF_MONTH) );
		this.premium = premium;
		this.paid = paid;
		this.type = type;
		{ 
			for ( int counter = 1 ; counter <= 12*term ; counter++) {
			payments.add(new GregorianCalendar());
			}
			int year = 0;
			int month = startDate.get(Calendar.MONTH) ;
			for ( Calendar da : payments ) {
			    month++;
				if ( month == 13 ) {
					year++;
					month = 1;
				}
				da.set( Calendar.YEAR, year + startDate.get(Calendar.YEAR) );
				da.set( Calendar.MONTH, month );
				da.set( Calendar.DAY_OF_MONTH, startDate.get(Calendar.DAY_OF_MONTH) );
			}	
		}
		
		paymentStatus = new boolean[term*12];
	}
	
	public void setPolicyholderID(int policyholderID) {
		this.policyholderID = policyholderID;		
	}
	public void setDes(String policyDes) {
		this.policyDes = policyDes;
	}	
	public void setTerm(int term) {
		this.term = term;
		expiryDate.set( Calendar.YEAR, term + startDate.get(Calendar.YEAR) );
		{ 
			for ( int counter = 1 ; counter <= 12*term ; counter++) {
			payments.add(new GregorianCalendar());
			}
			int year = 0;
			int month = startDate.get(Calendar.MONTH) ;
			for ( Calendar da : payments ) {
			    month++;
				if ( month == 13 ) {
					year++;
					month = 1;
				}
				da.set( Calendar.YEAR, year + startDate.get(Calendar.YEAR) );
				da.set( Calendar.MONTH, month );
				da.set( Calendar.DAY_OF_MONTH, startDate.get(Calendar.DAY_OF_MONTH) );
			}	
		}
		paymentStatus = new boolean[term*12];
	}
	public void setPremium(int premium) {
		this.premium = premium;
	}
	public void setPaid(int paid) {
		this.paid = paid;
	}
	
	public int getPolicyID() {
		return policyID;
	}
	public int getPolicyholderID() {
		return policyholderID;
	}
	public String getPolicyDes() {
		return policyDes;
	}
	public int getTerm() {
		return term;
	}
	public Calendar getExpiryDate() {
		return expiryDate;
	}
	public int getPaid() {
		return paid;
	}
	public int getPremium() {
		return premium;
	}
	public String getType() {
		String types;
		if (type==1) 
			types= "Property";
		else if (type==2)
			types= "Life";
		else if (type==3)
			types= "Liability";
		else
			types= null;
		return types;
	}
	public String getPolicyholderName(ArrayList<Client> cl)
	{
		String name = null;
		for(Client ex: cl)
		{
			if(getPolicyholderID() == ex.getClientID()) {
				name = ex.getName();
			}
		}
	return name; 
	}

	public void makeClaim(Calendar date, int amount , String claim) {
		int num = claimDate.size();
		claimDate.add(num, date);
		claimPaid.add(num,amount);
		claimDes.add(num,claim);
		claimsPaid += amount ;
	}
	
	public void displayClaims(ArrayList<Client> cl) {
		int num = claimDate.size();
			System.out.print("\n————————————————————————————————————————————————————————————————————————————————————");
			System.out.printf("\nClient ID = %-12s\nClient Name = %-20s\n",getPolicyholderID() ,getPolicyholderName(cl) );
			System.out.println("—Claims—————————————————————————————————————————————————————————————————————————————"
					+ "\n NO      Date                 Paid        Description"
					+ "\n————————————————————————————————————————————————————————————————————————————————————");
			int c = 1;
			for ( int counter = 0 ; counter < num ; counter++) {
		    System.out.printf(" %02d      %tF           $%-11d%s\n", c, claimDate.get(counter), claimPaid.get(counter) , claimDes.get(counter));
		    c++;
		}
	    System.out.printf("————————————————————————————————————————————————————————————————————————————————————\n"
	    		+"Total paid claims: $%d"
	    		+"\n————————————————————————————————————————————————————————————————————————————————————\n", claimsPaid ); 
		}
	public String paymentStatus(int num) {
		return paymentStatus[num] ? "Paid" : "Unpaid";
	}
	public String paidStatus() {
		return ((premium*term*12)<=paid) ? "Paid" : "Unpaid";
	}
	
	public void pay( int num ) {
		paymentStatus[num-1] = true;
		paid += premium;
	}
	
	public void displayPayments(ArrayList<Client> cl) {
		System.out.print("\n———————————————————————————————");
		System.out.printf("\nClient ID = %-12s\nClient Name = %-20s\n",getPolicyholderID() ,getPolicyholderName(cl) );
		System.out.println("—Payments——————————————————————"
				+ "\n NO       Date           Status"
				+ "\n———————————————————————————————");
		int c = 1;
	for ( Calendar da : payments ) {
	    System.out.printf(" %03d      %tF     %s\n", c, da, paymentStatus(c-1));
	    c++;
	}
    System.out.printf("———————————————————————————————\n"
    		+"Premium = $%d"
    		+ "\nTotal paid amount: $%d\n"
    		+ "\nTotal due amount: $%d\n"
    		+"———————————————————————————————\n", premium , paid, premium*term*12-paid); 
	}
	
	public static void display(ArrayList<Policy> po, ArrayList<Client> cl) 
	{
		System.out.println("\n—————Policies List————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————");
		System.out.println(String.format("%-5s%-12s%-18s%-25s%-17s%-31s%-14s%-14s%-10s%-13s%-8s", "#No","Policy ID","Policyholder ID", "Policyholder Name","Insurance Type" ,"Policy Description","Policy Term","Expiry Date","Premium","Total Paid","Status"));
		System.out.println("——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————");
		int no=1;
		for(Policy ex : po)
		{
		if ( ex.term != 1)
		System.out.println(String.format("%-5s%-12s%-18s%-25s%-17s%-31s%-3syears      %-14tF$%-9s$%-12s%-8s", no,ex.getPolicyID(),ex.getPolicyholderID(),ex.getPolicyholderName(cl),ex.getType(),ex.getPolicyDes(),ex.getTerm(),ex.getExpiryDate(),ex.getPremium(),ex.getPaid(),ex.paidStatus()));
		else
		System.out.println(String.format("%-5s%-12s%-18s%-25s%-17s%-31s%-3syear       %-14tF$%-9s$%-12s%-8s", no,ex.getPolicyID(),ex.getPolicyholderID(),ex.getPolicyholderName(cl),ex.getType(),ex.getPolicyDes(),ex.getTerm(),ex.getExpiryDate(),ex.getPremium(),ex.getPaid(),ex.paidStatus()));
		no++;
		}
		System.out.println("——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————");
}
}
class Client implements Serializable {
	
	private int clientID;
	
	private String name;
	private int gender;
	private int age;
	private String phone;
	private String email;
	private String address;
	
	private int policy=0;
	
	public Client(int clientID, String name, int gender, int age, String phone, String email, String address)
	{
		this.clientID = clientID;
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.phone = phone;
		this.email = email;
		this.address = address;
	}

	public void setClientID(int clientID ) {
	this.clientID = clientID;
	}
	public void setName(String name) {
	this.name = name;
	}	
	public void setGender(int gender) {
	this.gender = gender;
	}	
	public void setAge(int age) {
	this.age = age;
	}	
	public void setPhone(String phone) {
	this.phone = phone;
	}	
	public void	setEmail(String email) {
	this.email = email;
	}	
	public void setAddress(String address) {
	this.address = address;
	}
	public void addPolicy() {
		policy++;
	}
	public void delPolicy() {
		policy--;
	}

	public int getClientID() {
		return clientID;
	}
	public String getName() {
		return name;
	}
	public String getGender() {
		String Gender;
		if (gender==1) 
			Gender= "male";
		else if (gender==2)
			Gender= "female";
		else
			Gender= null;
		return Gender;
	}
	public int getPolicy() {
		return policy;
	}
	public int getAge() {
		return age;
	}
	public String getPhone() {
		return phone;
	}
	public String getEmail() {
		return email;
	}
	public String getAddress() {
		return address;
	}
	public static void display(ArrayList<Client> cl)
	{
		System.out.println("\n—————Clients List—————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————");
		System.out.println(String.format("%-5s%-15s%-27s%-12s%-9s%-7s%-20s%-25s%-35s","#No","Client ID","Name","Policies","Gender","Age","Phone","Email","Address"));
		System.out.println("——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————");
		int no=1;
		for( Client ex : cl)
		{
		System.out.println(String.format("%-5s%-15s%-27s%-12s%-9s%-7s%-20s%-25s%-35s",no,ex.getClientID(),ex.getName(),ex.getPolicy(),ex.getGender(),ex.getAge(),ex.getPhone(),ex.getEmail(),ex.getAddress()));
		no++;
		}
		System.out.println("——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————");
		}
	
	public void displayPolicies(ArrayList<Policy> po) {
		System.out.print(String.format("\nClient ID = %-12s Client Name = %-20s",clientID ,name ));
		System.out.println("\n—————Policies List————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————");
		System.out.println(String.format("%-5s%-12s%-17s%-31s%-14s%-14s%-10s%-13s%-8s", "#No","Policy ID","Insurance Type" ,"Policy Description","Policy Term","Expiry Date","Premium","Total Paid","Status"));
		System.out.println("——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————");
		int no=1;	
		for(Policy ez: po)
			{
				if( clientID == ez.getPolicyholderID()) {
		if ( ez.getTerm() != 1)
		System.out.println(String.format("%-5s%-12s%-17s%-31s%-3syears      %-14tF$%-9s$%-12s%-8s", no,ez.getPolicyID(),ez.getType(),ez.getPolicyDes(),ez.getTerm(),ez.getExpiryDate(),ez.getPremium(),ez.getPaid(),ez.paidStatus()));
		else
		System.out.println(String.format("%-5s%-12s%-17s%-31s%-3syear       %-14tF$%-9s$%-12s%-8s", no,ez.getPolicyID(),ez.getType(),ez.getPolicyDes(),ez.getTerm(),ez.getExpiryDate(),ez.getPremium(),ez.getPaid(),ez.paidStatus()));
		no++;
				}
			}
		System.out.println("——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————");
	}
}
public class InsuranceManagement {
	public static void main(String[] args) {
		
		Date today = new Date();
		Date date = null;
	    SimpleDateFormat dateInput = new SimpleDateFormat ("yyyy-MM-dd");
		
		int policyID;
		int policyholderID;
		String policyDes;
		Calendar startDate = new GregorianCalendar();
		Calendar expiryDate;
		int term;
		int premium;
		int paid;
		int type;

		int clientID;
		String name;
		int gender;
		int age;
		String phone;
		String email;
		String address;
		
		Calendar paidDate = new GregorianCalendar();
		String claim;
		int claimPaid;
		
		ArrayList<Policy> po = new ArrayList<Policy>();
		ArrayList<Client> cl = new ArrayList<Client>();
		Scanner input = new Scanner(System.in);
		Scanner line = new Scanner(System.in);
		
		File file = null;
		FileInputStream fileInput = null;
		ObjectInputStream OIClient = null;
		ObjectInputStream OIPolicy = null;
		
		FileOutputStream fileOutput = null;
		ObjectOutputStream OOClient =null;
		ObjectOutputStream OOPolicy =null;

		try{
			file = new File("E:/Data/DataInsuranceManagement.txt");
			if(file.exists())
			{
				fileInput = new FileInputStream(file);
				OIClient = new ObjectInputStream(fileInput);
				OIPolicy = new ObjectInputStream(fileInput);
				po = (ArrayList<Policy>)OIClient.readObject();
				cl = (ArrayList<Client>)OIPolicy.readObject();
				System.out.println("DATA LOADED.");
			}	
		}
		catch(Exception exp){
			System.out.println("DATA NOT LOADED.");
		}
		
		
		
do {
	System.out.printf("\nMAIN MENU:"
	+"\n[1] Manage Clients."
	+"\n[2] Manage Policies."
	+"\n[3] Manage Payments."
	+"\n[0] Save & Exit.");
	System.out.print("\nSELECT : ");
	switch (input.nextInt()) {
	case 1:
		System.out.printf("\nManage Clients:"
		+"\n[1] Add a Client."
		+"\n[2] Edit a Client."
		+"\n[3] Delete a Client."
		+"\n[4] Veiw Clients Details."
		+"\n[5] Veiw a Client Policies."
		+"\n[0] MAIN NENU."
		+"\nSELECT : ");
		switch (input.nextInt()) {
		case 1:
			System.out.println("\nEnter the following details: \n");
			int i=0;
			int j=0;
			System.out.print("Enter client ID: ");
			clientID = input.nextInt();
				for(Client ex: cl) {
					if(clientID == ex.getClientID()) {
						System.out.print("\nThe client ID is not available, Try again!\n");					
						j++;
					}
				}
				if (j == 0) {
				System.out.print("Enter name: ");
				name = line.nextLine();
				System.out.print("\nEnter gender: "
				+"\n[1] male."
				+"\n[2] female."
				+"\nSELECT : ");
				gender = input.nextInt();
				System.out.print("\nEnter age: ");
				age = input.nextInt();
				System.out.print("Enter phone: ");
				phone = line.nextLine();
				System.out.print("Enter email: ");
				email = line.nextLine();
				System.out.print("Enter address: ");
				address = line.nextLine();
				cl.add(new Client(clientID, name, gender, age, phone, email, address));
				Client.display(cl);
					break;
			}
					break;
		case 2:
			System.out.print("\nEnter the client ID to EDIT the details: ");
			clientID = input.nextInt();
			i=0;
			j=0;
				for(Client ex: cl) {
					if(clientID == ex.getClientID()) {	
					j++;
					do{
				System.out.print("\nEDIT Client details :" +
				"\n[1] Name." +
				"\n[2] Gender." +
				"\n[3] Age." +
				"\n[4] Phone." +
				"\n[5] Email." +
				"\n[6] Address." +				
				"\n[0] MAIN NENU."+
				"\nSELECT: ");
				switch(input.nextInt())	{
				case 1: 
					System.out.print("Enter new Name: ");
						ex.setName(line.nextLine());
						System.out.println("\nDone!");
						break;
				case 2: 
					System.out.print("Enter new gender:"
						+"\n[1] male."
						+"\n[2] female."
						+"\nSELECT: ");
						ex.setGender(input.nextInt());					
						System.out.println("\nDone!");
						break;
				case 3: 
					System.out.print("Enter new age: ");
						ex.setAge(input.nextInt());
						System.out.println("\nDone!");
						break;
				case 4: 
					System.out.print("Enter new phone number :");
						ex.setPhone(line.nextLine());
						System.out.println("\nDone!");
						break;		
				case 5: 
					System.out.print("Enter new email: ");
						ex.setEmail(line.nextLine());
						System.out.println("\nDone!");
						break;		
				case 6: 
					System.out.print("Enter new address: ");
						ex.setAddress(line.nextLine());
						System.out.println("\nDone!");
						break;		
				case 0:
					j++;
						break;
						
				default : System.out.println("\nEnter a correct choice from the List :");
						break;
				}
				}
			while(j==1);
			}
		}
		if(j == 0)
		{
			System.out.println("\nClient ID id not available, Please enter a valid ID!");
		}
		break;
				
		case 3: System.out.print("\nEnter client ID to DELETE from the database: ");
		clientID = input.nextInt();
		int k=0;
			try{for(Client ex: cl) {
				if(clientID == ex.getClientID()) {
				cl.remove(ex);
				try{for(Policy ez: po) {
					if(clientID == ez.getPolicyholderID()) {
					po.remove(ez);
					}
				}}
				catch(Exception ez){
					System.out.println(ez);}
				System.out.println("\nThe client deleted!");
				k++;
				}
			}
			if(k == 0)
			{
			System.out.println("\nThe client ID is not available, Please enter a valid ID!");
			}}
			catch(Exception ex){
				System.out.println(ex);
			}
		break;
		
		case 4: Client.display(cl);
		break;
		
		case 5: System.out.print("\nEnter client ID to display policies: ");
		clientID = input.nextInt();
		k=0;
		try { for(Client ex: cl) {
			if(clientID == ex.getClientID()) {
				ex.displayPolicies(po);
				k++;
			}}
			if(k == 0)
			{
			System.out.println("\nThe client ID is not available, Please enter a valid ID!");
		}}
		catch(Exception ex){
				System.out.println(ex);
		}
		break;
		default:
		break;
		}
	break;
	case 2:
		System.out.printf("\nManage Policies:"
		+"\n[1] Add a policy."
		+"\n[2] Edit a Policy."
		+"\n[3] Delete a Policy."
		+"\n[4] Veiw Policy Details."
		+"\n[0] MAIN NENU.");
		System.out.print("\nSELECT : ");
		switch (input.nextInt()) {
		case 1:
			System.out.println("\nEnter the following details: \n");
			System.out.print("Enter policyholder ID: ");
			policyholderID = input.nextInt();
			int i=0;
			int j=0;
				for(Client ex: cl) {	
					if(policyholderID == ex.getClientID()) {
					System.out.println("Found: "+ ex.getName()+".");
					i++;
					System.out.print("\nEnter policy ID: ");
					policyID = input.nextInt();
					for(Policy ez: po) {	
						if(policyID == ez.getPolicyID()) {
						System.out.print("\nThe client ID is not available, Try again!\n");					
						j++;
						}
					}
						if (j == 0) {
						System.out.printf("\nINSURANCE TYPE:"
						+"\n[1] Property insurance."
						+"\n[2] Life insurance."
						+"\n[3] Liability insurance."
						+"\nSELECT : ");
						type = input.nextInt();
						System.out.print("\nEnter policy description: ");
						policyDes = line.nextLine();
						System.out.print("Enter start date of policy [YYYY-MM-DD]: ");
						try {
						date = dateInput.parse(line.nextLine());
						startDate.setTime(date);
						}
						catch (ParseException e) {
					    System.out.println("Unparseable using. Set today as start date. \n");
						startDate.setTime(today);
						}
						System.out.print("Enter policy term by year: ");
						term = input.nextInt();
						System.out.print("Enter premium per month: ");
						premium = input.nextInt();
						paid = 0;
						po.add(new Policy(policyID, policyholderID, policyDes, startDate, term, premium, paid, type));
						ex.addPolicy();
						Policy.display(po,cl);
						break;
						}
					}
				}
					if (i == 0) {
					System.out.print("\nThe client ID is not available, Try again!\n");
					}
				break;
				
		case 2:
			System.out.print("\nEnter the policy ID to EDIT the details: ");
			policyID = input.nextInt();
			i=0;
			j=0;
				for(Policy ex: po) {
					if(policyID == ex.getPolicyID()) {	
					j++;
					do{
				System.out.print("\nEDIT policy details: " +
				"\n[1] Description." +
				"\n[2] Term years." +
				"\n[3] Premium." +
				"\n[0] MAIN NENU."+
				"\nSELECT: ");
				switch(input.nextInt())	{
				case 1:
					System.out.print("\nEnter new policy description: ");
						ex.setDes(line.nextLine());
						System.out.println("\nDone!");
						break;
				case 2: 
					System.out.print("Enter new policy term by year: ");
						ex.setTerm(input.nextInt());
						System.out.println("\nDone!");
						break;
				case 3: 
					System.out.print("Enter new premium: ");
						ex.setPremium(input.nextInt());					
						System.out.println("\nDone!");
						break;		
				case 0:
					j++;
						break;
						
				default : System.out.println("\nEnter a correct choice from the List :");
						break;
				}
				}
			while(j==1);
			}
		}
		if(j == 0)
		{
			System.out.println("\nEmployee Details are not available, Please enter a valid ID!!");
		}
	
		break;
				
		case 3: System.out.print("\nEnter policy ID to DELETE from the database: ");
			policyID = input.nextInt();
			int k=0;
			try{for(Policy ex: po) {
					if(policyID == ex.getPolicyID()) {
					po.remove(ex);
					System.out.println("\nThe policy deleted!");
					Policy.display(po,cl);
					for(Client ez: cl) {
						if(ex.getPolicyholderID() == ez.getClientID()) {
							ez.delPolicy();
						}
					}
					k++;
					}
				}
				if(k == 0)
				{
				System.out.println("\nThe policy ID is not available, Please enter a valid ID!");
				}}
			catch(Exception ex){
				System.out.println(ex);
			}
		break;
		case 4: Policy.display(po,cl);
		break;
		
		default:
		break;
		}
	break;
	case 3:
		System.out.println("\nManage Payments:"
		+"\n[1] Pay a Premium."
		+"\n[2] Veiw Payments Details."
		+"\n[3] Make a Claim."
		+"\n[4] Veiw Claims Details."
		+"\n[0] MAIN NENU.");
		System.out.print("\nSELECT : ");
		switch(input.nextInt())	{
		case 1:
			System.out.print("\nEnter policy ID to pay: ");
			policyID = input.nextInt();
			int k=0;
			for(Policy ex: po) {
					if(policyID == ex.getPolicyID()) {
					System.out.print("\nEnter payment number: ");
					ex.pay(input.nextInt());
					k++;
					System.out.println("\nPaid!");
					}
				}
				if(k == 0)
				{
				System.out.println("\nThe policy ID is not available, Please enter a valid ID!\n");
				}
		break;
		case 2:
			System.out.print("\nEnter policy ID to display payments: ");
			policyID = input.nextInt();
			k=0;
			for(Policy ex: po) {
					if(policyID == ex.getPolicyID()) {
					ex.displayPayments(cl);
					k++;
					}
				}
				if(k == 0)
				{
				System.out.println("\nThe policy ID is not available, Please enter a valid ID!\n");
				}
		break;
		case 3:
			System.out.print("\nEnter policy ID to make a claim: ");
			policyID = input.nextInt();
			k=0;
			for(Policy ex: po) {
					if(policyID == ex.getPolicyID()) {
						System.out.print("Enter date of claim [YYYY-MM-DD]: ");
						try {
						date = dateInput.parse(line.nextLine());
						paidDate.setTime(date);
						}
						catch (ParseException e) {
						System.out.println("Unparseable using. Set today as start date. \n");
						paidDate.setTime(today);
						}						
						System.out.print("Enter description of claim: ");
						claim = line.nextLine();
						System.out.print("Enter paid claim amount: ");
						claimPaid = input.nextInt();
					ex.makeClaim( paidDate , claimPaid , claim);
					System.out.print("\nDone!\n");
					ex.displayClaims(cl);
					k++;
					}
				}
				if(k == 0)
				{
				System.out.println("\nThe policy ID is not available, Please enter a valid ID!\n");
				}
		break;
		case 4:
			System.out.print("\nEnter policy ID to display claims: ");
			policyID = input.nextInt();
			k=0;
			for(Policy ex: po) {
					if(policyID == ex.getPolicyID()) {
					ex.displayClaims(cl);
					k++;
					}
				}
				if(k == 0)
				{
				System.out.println("\nThe policy ID is not available, Please enter a valid ID!\n");
				}
		break;
		}
	break;
	case 0:

		try {
			fileOutput = new FileOutputStream(file);
			OOClient = new ObjectOutputStream(fileOutput);
			OOPolicy = new ObjectOutputStream(fileOutput);
			OOClient.writeObject(po);
			OOPolicy.writeObject(cl);
		} 
		catch (IOException e1) {
			e1.printStackTrace();
		}
		catch(Exception e2){
			e2.printStackTrace();
		}
		
		finally{
			try {
				fileInput.close();
				OIClient.close();
				OIPolicy.close();
				fileOutput.close();
				OOClient.close();
				OOPolicy.close();
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
			
		}
			System.out.println("\nEXIT, Saving Files and closing the tool.");
			input.close();
			line.close();
			System.exit(0);
	break;		
	}
}
while(true);
}}