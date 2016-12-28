
name: ajang bul
CSID: u5o8
CPSC 304: 
Assignment 3:

Part 2: Everything works fine as per my eclipse tests. The queries also were tested in server and they also works;

   (i) Inserting an item into the item table was a bit straight forward, I just have to enter all the 
       attributes of the item starting with the primary key upc, sellingPrice, stock and whether it was
       or not.  
       
       Before inserting an item into the table, we have the following:

Welcome! Mr./Mrs. Manager/Clerk: Please choose one of the following: 
1.  To insert item
2.  To remove item
3.  To update item
4.  To Show item
5.  To get textbooks satisfying the two conditions given
6.  To find top three items
7.  Quit
>> 4
 
 
UPC            SELLINGPRICE   STOCK          TAXABLE         
a0016     10                  6                   y         
a00001    50                  100                 y         
a00002    60                  500                 y         
a00003    64                  200                 n         
a00004    800                 500                 y         
a00005    56                  130                 n         
a00006    66                  300                 n         
a00007    123                 320                 y         
a00008    100                 125                 y         
a00009    99                  159                 n         
a00010    76                  123                 y         
a00011    88                  144                 n         
a00012    50                  264                 y         
a00013    74                  155                 y         
a00014    24                  160                 y         
a00015    15                  8                   n         
a00017    34                  5                   n         
a00018    1000                200                 y         
a00019    112                 150                 n         
a00020    44                  2                   n         
a00021    99                  0                   y         


Welcome! Mr./Mrs. Manager/Clerk: Please choose one of the following: 
1.  To insert item
2.  To remove item
3.  To update item
4.  To Show item
5.  To get textbooks satisfying the two conditions given
6.  To find top three items
7.  Quit
>> 1
 

Item UPC: a00022

Item Price: 50

Item Stock: 2

Item Taxable: y

 After inserting, we have the following with new item added in row 2:

Welcome! Mr./Mrs. Manager/Clerk: Please choose one of the following: 
1.  To insert item
2.  To remove item
3.  To update item
4.  To Show item
5.  To get textbooks satisfying the two conditions given
6.  To find top three items
7.  Quit
>> 4
 
 
UPC            SELLINGPRICE   STOCK          TAXABLE         
a0016     10                  6                   y         
a00022    50                  2                   y         
a00001    50                  100                 y         
a00002    60                  500                 y         
a00003    64                  200                 n         
a00004    800                 500                 y         
a00005    56                  130                 n         
a00006    66                  300                 n         
a00007    123                 320                 y         
a00008    100                 125                 y         
a00009    99                  159                 n         
a00010    76                  123                 y         
a00011    88                  144                 n         
a00012    50                  264                 y         
a00013    74                  155                 y         
a00014    24                  160                 y         
a00015    15                  8                   n         
a00017    34                  5                   n         
a00018    1000                200                 y         
a00019    112                 150                 n         
a00020    44                  2                   n         
a00021    99                  0                   y   

   (ii) removing an item: Because of the unique identity constraint, I have implement my function such that 
        you have to delete an item first in itemPurchase and book if the have same key before deleting from  
        deleting from item (the parent):
     
       before removing an item, note a00001 at row 3:


     
UPC            SELLINGPRICE   STOCK          TAXABLE         
a0016     10                  6                   y         
a00022    50                  2                   y         
a00001    50                  100                 y         
a00002    60                  500                 y         
a00003    64                  200                 n         
a00004    800                 500                 y         
a00005    56                  130                 n         
a00006    66                  300                 n         
a00007    123                 320                 y         
a00008    100                 125                 y         
a00009    99                  159                 n         
a00010    76                  123                 y         
a00011    88                  144                 n         
a00012    50                  264                 y         
a00013    74                  155                 y         
a00014    24                  160                 y         
a00015    15                  8                   n         
a00017    34                  5                   n         
a00018    1000                200                 y         
a00019    112                 150                 n         
a00020    44                  2                   n         
a00021    99                  0                   y         


Welcome! Mr./Mrs. Manager/Clerk: Please choose one of the following: 
1.  To insert item
2.  To remove item
3.  To update item
4.  To Show item
5.  To get textbooks satisfying the two conditions given
6.  To find top three items
7.  Quit
>> 2
 

itemPurchase upc: a00001

book upc: a00001

item upc: a00001


Welcome! Mr./Mrs. Manager/Clerk: Please choose one of the following: 
1.  To insert item
2.  To remove item
3.  To update item
4.  To Show item
5.  To get textbooks satisfying the two conditions given
6.  To find top three items
7.  Quit
>> 4
 You can see it’s gone! Also in the other tables!
 
UPC            SELLINGPRICE   STOCK          TAXABLE         
a0016     10                  6                   y         
a00022    50                  2                   y         
a00002    60                  500                 y         
a00003    64                  200                 n         
a00004    800                 500                 y         
a00005    56                  130                 n         
a00006    66                  300                 n         
a00007    123                 320                 y         
a00008    100                 125                 y         
a00009    99                  159                 n         
a00010    76                  123                 y         
a00011    88                  144                 n         
a00012    50                  264                 y         
a00013    74                  155                 y         
a00014    24                  160                 y         
a00015    15                  8                   n         
a00017    34                  5                   n         
a00018    1000                200                 y         
a00019    112                 150                 n         
a00020    44                  2                   n         
a00021    99                  0                   y         



 
Part 3: Few assumptions made: last week assumed to have been the period between Oct 25th and Oct 31: What 
        went wrong: I did text for the case where we should not delete an item when the stock is greater 
        than zero. I didn’t quite understand that. Other than that, the program run just fine and in tables 
        I have the following output after pressing 5:
         
Welcome! Mr./Mrs. Manager/Clerk: Please choose one of the following: 
1.  To insert item
2.  To remove item
3.  To update item
4.  To Show item
5.  To get textbooks satisfying the two conditions given
6.  To find top three items
7.  Quit
>> 5
 
 
UPC            TITLE          QUANTITY        
a00009         The Martian                                       140            
a00002         Secrets of Silicon Valley                         100            
a00007         Art of Programming                                74             
a00015         History of Civilization                           70             
a00014         Electromagnetism                                  65    

        


Part 4: Part 4 works fine according to the implementation I have in the eclipse, I have the following output
        after pressing 6 in the console manager/clerk user manual:

Welcome! Mr./Mrs. Manager/Clerk: Please choose one of the following: 
1.  To insert item
2.  To remove item
3.  To update item
4.  To Show item
5.  To get textbooks satisfying the two conditions given
6.  To find top three items
7.  Quit
>> 6
 
 
UPC            TITLE          TOTALAMT        
a00007         Art of Programming                                369.0          
a00009         The Martian                                       198.0          
a00014         Electromagnetism                                  72.0           

Thank you.
end;
