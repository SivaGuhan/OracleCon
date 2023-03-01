import java.sql.*;
import java.io.*;
import java.util.*;
class OracleCon{
	public static void main(String args[]){
		Scanner sc=new Scanner(System.in);
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","siva");
			Statement stmt=con.createStatement();
			System.out.println("Basic Operations!!!");
			String query="";
			int ch;
			do{
				System.out.println("1. Create");
				System.out.println("2. Insert");
				System.out.println("3. Read");
				System.out.println("4. Update");
				System.out.println("5. Delete");
				System.out.println("6. Call Procedure");
				System.out.println("7. Call Function");
				System.out.println("8. Exit");
				System.out.print("Enter your choice : ");
				ch=sc.nextInt();
				switch(ch){
					case 1:
						System.out.println("create table product(product_id varchar(5) primary key, product_name varchar(20), cost float(4))");
						query="create table product(product_id varchar(5) primary key, product_name varchar(20), cost float(4))";
						stmt.executeUpdate(query);
						System.out.println("Table created successfully");
						System.out.println();
						break;
					case 2:
						PreparedStatement psmt=con.prepareStatement("insert into product values(?,?,?)");
						BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
						while(true) {
							System.out.print("Enter Product Id : ");
							int product_id=Integer.parseInt(br.readLine());
					
							System.out.print("Enter Product Name : ");
							String product_name=br.readLine();
					
							System.out.print("Enter Product Cost : ");
							double product_cost=Double.parseDouble(br.readLine());
					
							psmt.setInt(1, product_id);
							psmt.setString(2,product_name);
							psmt.setDouble(3, product_cost);
					
							int count=psmt.executeUpdate();
							if(count>0)
								System.out.println(count+" record inserted");
							else
								System.out.println("No record inserted");
					
							System.out.print("Do you want to insert more records[Yes/No] : ");
					
							String ch1=br.readLine();
							if(ch1.equalsIgnoreCase("no"))
								break;
						}
						System.out.println();
						break;
					case 3:
						ResultSet rs=stmt.executeQuery("select * from product");
						while(rs.next()) {
							System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getDouble(3));
						}
						System.out.println();
						break;
					case 4:
						BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
			
						System.out.print("Enter Product Id : ");
						String value1=br1.readLine();
					
						System.out.print("Enter New Cost : ");
						String value2=br1.readLine();
						double cost=Double.parseDouble(value2);
			
						int count=stmt.executeUpdate("update product set cost=" + cost+ "where product_id="+ value1);
						if(count>0)
							System.out.println(count+" rows updated");
						else
							System.out.println("No record found");
						System.out.println();
						break;
					case 5:
						BufferedReader br2=new BufferedReader(new InputStreamReader(System.in));
		
						while(true) {
							System.out.print("Enter Product Id of product which is to be deleted : ");
							String prodid=br2.readLine();
		
							int count1=stmt.executeUpdate("delete from product where product_id="+prodid);
							if(count1==1)
								System.out.println(count1+" rows deleted");
							else
								System.out.println("No record deleted");
			
							System.out.print("Do you want to delete more records[Yes/No] : ");
			
							String ch2=br2.readLine();
							if(ch2.equalsIgnoreCase("no"))
								break;
				
						}
						System.out.println();
						break;
					case 6:
						System.out.println("Procedure 1");
						BufferedReader br3=new BufferedReader(new InputStreamReader(System.in));
						System.out.print("Enter Product Id of product which has to be updated : ");
						String prodid1=br3.readLine();
						System.out.print("Enter New Cost : ");
						double cost1=Double.parseDouble(br3.readLine());
						CallableStatement ctmt=con.prepareCall("{call UPDATER(?,?)}");
						ctmt.setString(1,prodid1);
						ctmt.setDouble(2,cost1);
						ctmt.execute();
						System.out.println("Successfully Updated using Procedure");
						System.out.println("Procedure 2");
						System.out.print("Enter Product Id : ");
						String prodid3=br3.readLine();
						System.out.print("Enter Product Name : ");
						String prodname=br3.readLine();
						System.out.print("Enter Cost : ");
						double cost2=Double.parseDouble(br3.readLine());
						CallableStatement ctmt2=con.prepareCall("{call insertr(?,?,?)}");
						ctmt2.setString(1,prodid3);
						ctmt2.setString(2,prodname);
						ctmt2.setDouble(3,cost2);
						ctmt2.execute();
						System.out.println("Successfully Inserted using Procedure");
						System.out.println();
						break;
					case 7:
						System.out.println("Function 1");
						BufferedReader br4=new BufferedReader(new InputStreamReader(System.in));
						System.out.print("Enter a Customer ID : ");
						String prodid2=br4.readLine();
						CallableStatement ctmt1=con.prepareCall("{?= call display(?)}");
						ctmt1.setString(2,prodid2);
						ctmt1.registerOutParameter(1,Types.VARCHAR);
						ctmt1.execute();
						System.out.println("The name of the customer is "+ctmt1.getString(1));
						System.out.println("Function 2");
						CallableStatement ctmt3=con.prepareCall("{?= call no_of_rows}");
						ctmt3.registerOutParameter(1,Types.VARCHAR);
						ctmt3.execute();
						System.out.println("The no of rows in the customer table is "+ctmt3.getInt(1));
						System.out.println();
						break;
				}
			}while(ch!=8);
			con.close();
		}catch(Exception e){ System.out.println(e);}

	}
}