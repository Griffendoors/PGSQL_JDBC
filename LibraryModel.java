/**
 *
 * Abstract methods in slides?
 *
 */

import java.sql.*;

import javax.swing.*;

import org.junit.Before;

public class LibraryModel {

	// For use in creating dialogs and making them modal
	private JFrame dialogParent;

	Connection con;

	public LibraryModel(JFrame parent, String userid, String password) {
		String url = "jdbc:postgresql:" + "//db.ecs.vuw.ac.nz/"+ userid+ "_jdbc";

		dialogParent = parent;


		try { Class.forName("org.postgresql.Driver"); } catch (ClassNotFoundException e) { e.printStackTrace(); }
		try {
			//con = DriverManager.getConnection(url, userid, password);
			con = DriverManager.getConnection(url, userid, password);

			con.setAutoCommit(false);

		} catch (SQLException e) { e.printStackTrace();	}


	}

	public String bookLookup(int isbn) {
		try {
			con.setAutoCommit(false);

		} catch (SQLException e1) { e1.printStackTrace(); }
		try {

			String string = "SELECT title, Edition_No, NumLeft, name, surname FROM author a, book b, book_author ba WHERE b.isbn = ? AND b.isbn = ba.isbn AND ba.authorid = a.authorid";
			PreparedStatement psql =  con.prepareStatement(string);
			psql.setInt(1, isbn);
			ResultSet rs = psql.executeQuery();

			String name;
			String surname;
			String title;
			String Edition_No;
			String NumLeft;
			String ret = "";

			while(rs.next()){
				title = rs.getString("title");
				name = rs.getString("name");
				surname = rs.getString("surname");
				Edition_No = rs.getString("Edition_No");
				NumLeft = rs.getString("NumLeft");

				ret += "Book with id: "+isbn+", "+ title +"Author:"+ name + surname + "; Edition: "+Edition_No+"; Number avail :"+NumLeft+"\n";
			}
			finishGood();
			return ret;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		finishBad();
		return "Error trying to display book with id"+isbn + ", failed.";
	}

	public String showCatalogue() {
		try {
			con.setAutoCommit(false);

		} catch (SQLException e1) { e1.printStackTrace(); }

		try {

			String string = "SELECT title, name, surname FROM author a, book b, book_author ba WHERE b.isbn = ba.isbn AND ba.authorid = a.authorid";
			Statement sql = con.createStatement();
			ResultSet rs = sql.executeQuery(string);

			String title;
			String name;
			String surname;
			String ret = "";

			while(rs.next()){
				title = rs.getString("title");
				name = rs.getString("name");
				surname = rs.getString("surname");
				ret += "Title: "+ title +"Author:"+ name + surname +"\n";
			}
			finishGood();
			return ret;


		} catch (SQLException e) {
			e.printStackTrace();
			}

		finishBad();
		return "Error trying to display catalouge, failed.";
	}

	public String showLoanedBooks() {
		try {
			con.setAutoCommit(false);

		} catch (SQLException e1) { e1.printStackTrace(); }
		try{

			String string = "SELECT title, F_Name, L_Name, DueDate FROM book b, customer c, Cust_Book cb WHERE b.isbn = cb.isbn  AND cb.CustomerId = c.CustomerId";
			Statement sql = con.createStatement();
			ResultSet rs = sql.executeQuery(string);

			String title;
			String F_Name;
			String L_Name;
			String DueDate;
			String ret = "";

			while(rs.next()){
				title = rs.getString("title");
				F_Name = rs.getString("F_Name");
				L_Name = rs.getString("L_Name");
				DueDate = rs.getString("DueDate");

				ret += title+" on loan to: "+F_Name+L_Name+"; to be returned: "+DueDate+"\n" ;

			}
			finishGood();
			return ret;


		} catch (SQLException e) {
			e.printStackTrace();
			}

		finishBad();
		return "Error trying to display loaned books, failed.";
	}


	public String showAuthor(int authorID) {
		try {
			con.setAutoCommit(false);

		} catch (SQLException e1) { e1.printStackTrace();}

		try {

			String string = "SELECT name, surname, COUNT(ISBN) as c FROM author a, book_author ba WHERE a.AuthorId = ? AND a.AuthorId = ba.AuthorId GROUP BY a.name, a.surname";
			PreparedStatement psql =  con.prepareStatement(string);
			psql.setInt(1, authorID);
			ResultSet rs = psql.executeQuery();

			String name;
			String surname;
			String ret = "";
			String count;

			while(rs.next()){
				name = rs.getString("name");
				surname = rs.getString("surname");
				count = rs.getString("c");
				ret += "Author with id:	"+authorID+"; "+ name + surname +"Written "+count+" books"+"\n";
			}
			finishGood();
			return ret;


		} catch (SQLException e) {
			e.printStackTrace();
			}

		finishBad();
		return "Error trying to display author with id"+authorID + ", failed.";
	}

	public String showAllAuthors() {
		try {

			con.setAutoCommit(false);

		} catch (SQLException e1) {e1.printStackTrace();}

		try {

			String string = "SELECT name, surname, COUNT(ISBN) as c FROM author a, book_author ba WHERE a.AuthorId = ba.AuthorId GROUP BY a.name, a.surname";
			Statement sql = con.createStatement();
			ResultSet rs = sql.executeQuery(string);

			String name;
			String surname;
			String ret = "";
			String count;

			while(rs.next()){
				name = rs.getString("name");
				surname = rs.getString("surname");
				count = rs.getString("c");
				ret += "Author:	"+ name + surname +"Written "+count+" books"+"\n";
			}
			finishGood();
			return ret;



		} catch (SQLException e) {
			e.printStackTrace();
			}

		finishBad();
		return "Error trying to display Customers, failed.";
	}

	public String showCustomer(int customerID) {
		try {

			con.setAutoCommit(false);

		} catch (SQLException e1) { e1.printStackTrace();}

		try {
			String string = "Select F_Name, L_Name, City FROM Customer c WHERE c.CustomerID = ?";
			PreparedStatement psql = con.prepareStatement(string);
			psql.setInt(1, customerID);
			ResultSet rs = psql.executeQuery();

			String F_Name;
			String L_Name;
			String city;
			String ret = "";

			while(rs.next()){
				F_Name = rs.getString("F_Name");
				L_Name = rs.getString("L_Name");
				city = rs.getString("City");
				ret += "Customer with id: "+customerID+" , "+F_Name+" "+L_Name+" - "+city+"\n";
			}
			finishGood();
			return ret;



		} catch (SQLException e) {
			e.printStackTrace();
			}

		finishBad();
		return "Error trying to display customer with id"+customerID + ", failed.";
	}

	public String showAllCustomers() {
		try {

			con.setAutoCommit(false);
		} catch (SQLException e1) { e1.printStackTrace();}

		try {
			String string = "Select F_Name, L_Name, City FROM Customer";
			Statement sql = con.createStatement();
			ResultSet rs = sql.executeQuery(string);

			String F_Name;
			String L_Name;
			String City;
			String ret = "";

			while(rs.next()){
				F_Name = rs.getString("F_Name");
				L_Name = rs.getString("L_Name");
				City = rs.getString("City");
				ret += "Customer: "+F_Name+" "+L_Name+" - "+City+"\n";
			}
			finishGood();
			return ret;


		} catch (SQLException e) {
			e.printStackTrace();
			}

		finishBad();
		return "Error trying to display Customers, failed.";
	}

	public String borrowBook(int isbn, int customerID,int day, int month, int year) {
		try {
			con.setAutoCommit(false);

		} catch (SQLException e1) { e1.printStackTrace(); }
		try {

		String serial = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE";
		Statement sql = con.createStatement();
		sql.execute(serial);


		String string = "SELECT * FROM customer c WHERE c.customerID = ?";
		PreparedStatement psql = con.prepareStatement(string);
		psql.setInt(1, customerID);
		ResultSet rs = psql.executeQuery();

		String exists = null;

		while(rs.next()){
			 exists = rs.getString("F_Name");
		}
		if(exists == null){
			finishBad();
			return "Customer does not exist!";
		}

		else return customerFoundBorrow(isbn,customerID,day,month,year);

	} catch (SQLException e) { e.printStackTrace(); }

		finishBad();
		return "Error trying to process borrow transacton : thrown in borrowBook()";
	}

	public String customerFoundBorrow(int isbn, int customerID,int day, int month, int year){
		try {

//			String lock = "LOCK TABLE customer IN ROW EXCLUSIVE MODE";
//			Statement sql = con.createStatement();
//			sql.execute(lock);

			String string = "SELECT * FROM book b WHERE b.isbn = ?";
			PreparedStatement psql = con.prepareStatement(string);
			psql.setInt(1, isbn);
			ResultSet rs = psql.executeQuery();

			int exists = 0;
			int NumLeft = 0;

			while(rs.next()){
				 exists = rs.getInt("isbn");
				 NumLeft = rs.getInt("NumLeft");
			}
			if(exists == 0){
				finishBad();
				return "Book does not exist!";
			}
			else if(NumLeft == 0){
				finishBad();
				return "There are no copies of this book available";
			}
			else return bookFoundBorrow(isbn,customerID,day,month,year);

		} catch (SQLException e) {e.printStackTrace();}

		finishBad();
		return "Error trying to process borrow transacton : thrown in customerFoundBorrow()";
	}


	public String bookFoundBorrow(int isbn, int customerID,int day, int month, int year){

		try {
//			String lock = "LOCK TABLE book IN ROW EXCLUSIVE MODE";
//			Statement sql = con.createStatement();
//			sql.execute(lock);


			Date returnDate = new Date(year,month,day);

			String insert = "INSERT INTO Cust_Book VALUES(?,?,?)";
			PreparedStatement psql = con.prepareStatement(insert);
			psql.setInt(3, customerID);
			psql.setDate(2, returnDate);
			psql.setInt(1, isbn);
			int returnvalue = psql.executeUpdate();
			if(returnvalue == 0){
				finishBad();
				return "could not update Cust_Book table";
			}


		    JOptionPane pane = new JOptionPane("Confirm operation?");
		        Object[] options = new String[] { "Confirm", "Cancel" };
		        pane.setOptions(options);
		        JDialog dialog = pane.createDialog(dialogParent, "Dilaog");
		        dialog.show();
		        Object obj = pane.getValue();
		        int result = -1;
		        for (int k = 0; k < options.length; k++){
		        	 if (options[k].equals(obj))  result = k;	// 0 = confirm, 1 = cancel
		        }
		        if(result == 0 ){

		        	String update = "UPDATE Book SET NumLeft = NumLeft - 1 WHERE isbn = ? ";
		        	PreparedStatement psql2 = con.prepareStatement(update);
		        	psql2.setInt(1,isbn);
		        	int returnvalue2 = psql2.executeUpdate();
		        	if(returnvalue2 == 0){
		        		finishBad();
		        		return "Could not update books table";
		        	}
		        	else{
		        		finishGood();
		        		return "Borrow transaction complete, Book is due on "+ returnDate;
		        	}
		        }
		        else{
		        	finishBad();
		        	return "operation not confirmed, transaction cancelled";
		        }

		}catch (SQLException e) {e.printStackTrace();}

		finishBad();
		return "Error trying to process borrow transacton : thrown in bookFoundBorrow()";
	}




	public String returnBook(int isbn, int customerid) {
		try {
			con.setAutoCommit(false);

		} catch (SQLException e1) { e1.printStackTrace();
		}

		try {
		String serial = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE";
		Statement sql = con.createStatement();
		sql.execute(serial);


		String string = "SELECT * FROM customer c WHERE c.customerID = ?";
		PreparedStatement psql = con.prepareStatement(string);
		psql.setInt(1, customerid);
		ResultSet rs = psql.executeQuery();

		String exists = null;

		while(rs.next()){
			 exists = rs.getString("F_Name");
		}
		if(exists == null){
			finishBad();
			return "Customer does not exist!";
		}

		else return customerFoundReturn(isbn,customerid);

	} catch (SQLException e) { e.printStackTrace(); }

		finishBad();
		return "Error trying to process borrow transacton : thrown in returnBook()";

	}



	public String customerFoundReturn(int isbn,int customerID){
		try {
//			String lock = "LOCK TABLE customer IN ROW EXCLUSIVE MODE";
//			Statement sql = con.createStatement();
//			sql.execute(lock);

			String string = "SELECT * FROM book b WHERE b.isbn = ?";
			PreparedStatement psql = con.prepareStatement(string);
			psql.setInt(1, isbn);
			ResultSet rs = psql.executeQuery();

			int exists = 0;
			int NumLeft = 0;

			while(rs.next()){
				 exists = rs.getInt("isbn");
				 NumLeft = rs.getInt("NumLeft");
			}
			if(exists == 0){
				finishBad();
				return "Book does not exist!";
			}
			else return bookFoundReturn(isbn,customerID);

		} catch (SQLException e) {e.printStackTrace();}

		finishBad();
		return "Error trying to process borrow transacton : thrown in customerFoundReturn()";
	}


	public String bookFoundReturn(int isbn,int customerID){
		try {
//			String lock = "LOCK TABLE book IN ROW EXCLUSIVE MODE";
//			Statement sql = con.createStatement();
//			sql.execute(lock);

			String delete = "DELETE FROM cust_book WHERE isbn = ?";
			PreparedStatement psql = con.prepareStatement(delete);
			psql.setInt(1,isbn);
			int returnvalue = psql.executeUpdate();
			if(returnvalue == 0){
				finishBad();
				return "Could not return book, transaction was not completed";
			}

        	String update = "UPDATE Book SET NumLeft = NumLeft + 1 WHERE isbn = ? ";
        	PreparedStatement psql2 = con.prepareStatement(update);
        	psql2.setInt(1,isbn);
        	int returnvalue2 = psql2.executeUpdate();

        	if(returnvalue2 == 0){
        		finishBad();
        		return "Could not update books table";
        	}
        	else{
        		finishGood();
        		return "Borrow transaction complete, Book has been returned";
        	}


		} catch (SQLException e) {e.printStackTrace();}

		finishBad();
		return "Error trying to process borrow transacton : thrown in bookFoundReturn()";
	}



	public void closeDBConnection() {
		try {
			finishGood();
			con.close();

		} catch (SQLException e) {e.printStackTrace();}
	}





	public String deleteCus(int customerID) {
		try {
			con.setAutoCommit(false);

		} catch (SQLException e1) { e1.printStackTrace();}

		try {

			String delete = "DELETE FROM customer WHERE customerID = ?";
			PreparedStatement psql =  con.prepareStatement(delete);
			psql.setInt(1, customerID);
			int returnValue = psql.executeUpdate();
			if(returnValue == 0){
				finishBad();
				return "Delete customer transaction failed, customer not found";
			}
			else{
				finishGood();
				return "Transaction complete, customer removed";
			}



		} catch (SQLException e) {
			e.printStackTrace();
			}

		finishBad();
		return "Error completing Delete customer transaction";
	}

	public String deleteAuthor(int authorID) {
		try {
			con.setAutoCommit(false);

		} catch (SQLException e1) { e1.printStackTrace();}

		try {

			String delete = "DELETE FROM author WHERE authorID = ?";
			PreparedStatement psql =  con.prepareStatement(delete);
			psql.setInt(1, authorID);
			int returnValue = psql.executeUpdate();
			if(returnValue == 0){
				finishBad();
				return "Delete Author transaction failed, author not found";
			}
			else{
				finishGood();
				return "Transaction complete, Author removed";
			}



		} catch (SQLException e) {
			e.printStackTrace();
			}

		finishBad();
		return "Error completing Delete author transaction";
	}



	public String deleteBook(int isbn) {
		try {
			con.setAutoCommit(false);

		} catch (SQLException e1) { e1.printStackTrace();}

		try {

			String delete = "DELETE FROM book WHERE isbn = ?";
			PreparedStatement psql =  con.prepareStatement(delete);
			psql.setInt(1, isbn);
			int returnValue = psql.executeUpdate();
			if(returnValue == 0){
				finishBad();
				return "Delete Book transaction failed, author not found";
			}
			else{
				finishGood();
				return "Transaction complete, Book removed";
			}



		} catch (SQLException e) {
			e.printStackTrace();
			}

		finishBad();
		return "Error completing Delete Book transaction";
	}


	public void finishGood(){
		try {
			if(!con.getAutoCommit()){
				con.commit();
			}

			con.setAutoCommit(true);
		} catch (SQLException e) { e.printStackTrace();}

	}
	public void finishBad(){
		try {
			con.rollback();
			con.setAutoCommit(true);
		} catch (SQLException e) { e.printStackTrace();}

	}
}