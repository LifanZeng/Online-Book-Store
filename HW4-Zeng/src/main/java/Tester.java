import com.mongodb.*;
import com.mongodb.util.JSON;

import java.net.UnknownHostException;
import java.util.Scanner;

public class Tester {
    private  static MongoClient mongo;
    static {
        try { mongo = new MongoClient("localhost", 27017);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    private static DB db = mongo.getDB("book_store");
    private static DBCollection collection_Author, collection_Book, collection_Customer, collection_Order;
    //MongoCollection<Document> collection_B = db.getCollection("book");

    public static void main(String[] args) throws UnknownHostException {
        initialize();
        System.out.println("Connection to server successfully");
        System.out.println();
        System.out.println("Welcome to Book Store Application!");
        String selection;
        do{
            System.out.println("1. Insert books and users to database\n" +
                    "2. Retriever all information on a specific book\n" +
                    "3. Retrieve all information on the books searching by author's name\n" +
                    "4. Retrieve all information on a specific customer who created later than 2017-07-13 and live in San Jose\n" +
                    "5. Retrieve all information on books that have multiple publishers\n" +
                    "6. Retrieve all information on the books that have reviews\n"+
                    "7. Update a book information \n"+
                    "8. Exit");
            Scanner input = new Scanner(System.in);
            selection = input.next();
            switch(selection){
                case "1":   //Insert books and users to database
                    System.out.println("Input 'B' to insert a book, input 'U' to insert a user");
                    Scanner in_1 = new Scanner(System.in);
                    String selection_1 = in_1.next();
                    if (selection_1.equals("B") || selection_1.equals("b")){
                        String json_Book_test ="{ " +
                                "'title': 'Test Book Title'," +
                                "'authors': ['Author Test']," +
                                "'ISBN': '978-0000000001'," +
                                "'publisher': [{'name': 'OReilly Media', 'date': '2019-12-31', 'city': 'Sebastopol', 'address': " +
                                "[{'street': '1005 Gravenstein Highway North', 'city': 'Sebastopol', 'zip': 95472, 'state': 'CA', 'country': 'USA'}]}],"+
                                "'available': true,"+
                                "'pages': 514,"+

                                "'summary': 'In six parts, this book shows you how to: " +
                                "Work with MongoDB, perform write operations, find documents, and create complex queries" +
                                "Index collections, aggregate data, and use transactions for your application" +
                                "Configure a local replica set and learn how replication interacts with your application" +
                                "Set up cluster components and choose a shard key for a variety of applications" +
                                "Explore aspects of application administration and configure authentication and authorization" +
                                "Use stats when monitoring, back up and restore deployments, and use system settings when deploying MongoDB',"+

                                "'reviews': {'ratingNumber': 104, 'grades out of 5': 4.6, 'review_body':[{'customer': 'L. V. Smart', 'stars': 5, 'review': 'An excellent book " +
                                "on MongoDB. Well thought out with good examples that I could use IRL. I like how the book is partitioned - setting" +
                                " up MongoDB and then indexing, configuring, setting up clustering, admin and statistics. It flows very well.'}, " +
                                "{'customer': 'Mary Chen', 'stars': 4, 'review': 'This is a good MongoDB book.'}]},"+
                                "'language': 'English',"+
                                "'categories': ['Data Mining', 'Database Storage & Design', 'Data Processing']"+
                                "}";
                        DBObject dbObject_book_test =(DBObject) JSON.parse(json_Book_test);
                        collection_Book.insert(dbObject_book_test);
                    }
                    else if (selection_1.equals("U") || selection_1.equals("u")){
                        String json_customer_test ="{ " +
                                "'username': 'User Test'," +
                                "'password': 'test password'," +
                                "'active': true," +
                                "'address': [{'street': '2222 Century Blvd', 'city': 'Los Angle', 'zip': 90304, 'state': 'CA', 'country': 'USA'}],"+
                                "'date_of_creation':'2018-10-21',"+
                                "'orders': [{'books': [{'title':'MongoDB: The Definitive Guide: Powerful and Scalable Data Storage', 'ISBN': '978-1491954461'}, {'title': 'Java Garage', 'ISBN': '978-0321246233' }], 'order_date': '2020-06-13', 'delivery_date': '2020-06-17'}, " +
                                "{'books': [{'title':'Cassandra: The Definitive Guide: Distributed Data at Web Scale', 'ISBN': '978-1098115166'}], 'order_date': '2020-07-02', 'delivery_date': '2020-07-03'}]}";
                        DBObject dbObject_user =(DBObject) JSON.parse(json_customer_test);
                        collection_Customer.insert(dbObject_user);
                    }
                    else
                        System.out.println("Invalid input");
                    break;
                case "2":   //Retriever all information on a specific book
                    System.out.println("Bookname or ISBN: ");
                    Scanner in_2 = new Scanner(System.in);
                    String bookname_or_isbn = in_2.nextLine();

                    BasicDBObject query = new BasicDBObject();
                    query.put ("title", bookname_or_isbn);
                    DBCursor cursor = collection_Book.find(query);
                    while(cursor.hasNext()){
                        System.out.println(cursor.next());
                    }

                    BasicDBObject query2 = new BasicDBObject();
                    query2.put ("ISBN", bookname_or_isbn);
                    DBCursor cursor2 = collection_Book.find(query2);
                    while(cursor2.hasNext()){
                        System.out.println(cursor2.next());
                    }

                    System.out.println();
                    break;
                case "3":   //Retrieve all information on the books searching by author's name
                    System.out.println("Author's name: ");
                    Scanner in_3 = new Scanner(System.in);
                    String authorName = in_3.nextLine();
                    BasicDBObject queryByAuthor = new BasicDBObject();
                    queryByAuthor.put ("authors", new BasicDBObject("$elemMatch", new BasicDBObject("$eq", authorName))); //***!!!***
                    DBCursor cursor3 = collection_Book.find(queryByAuthor);
                    while(cursor3.hasNext()){
                        System.out.println(cursor3.next());
                    }
                    System.out.println();
                    break;
                case "4":   //Retrieve all information on a specific customer who created later than 2017-07-13 and live in San Jose
                    BasicDBObject query4 = new BasicDBObject();
//                    query4.put ("address", new BasicDBObject("$elemMatch", new BasicDBObject("city", "San Jose")));
//                    query4.put("date_of_creation", new BasicDBObject("$gt", "2017-07-13"));
                    query4.put ("address", new BasicDBObject("$elemMatch", new BasicDBObject("city", "San Jose")));
                    query4.append("date_of_creation", new BasicDBObject("$gt", "2017-07-13"));
                    DBCursor cursor4 = collection_Customer.find(query4);
                    while(cursor4.hasNext()){
                        System.out.println(cursor4.next());
                    }
                    System.out.println();
                    break;
                case "5":   //Retrieve all information on books that have multiple publishers
                    DBObject query5=BasicDBObjectBuilder.start().add("$where", "this.publisher.length > 1").get();
                    DBCursor cursor5 = collection_Book.find(query5);
                    if (cursor5.length()==0)
                        System.out.println("There are no books that have multiple publishers.\n");
                    else {
                        while (cursor5.hasNext()) {
                            System.out.println(cursor5.next());
                        }
                    }
                    break;
                case "6":   //Retrieve all information on the books that have reviews
                    BasicDBObject query6 = new BasicDBObject();
                    query6.put("reviews.ratingNumber", new BasicDBObject("$gt", 0));
                    DBCursor cursor6 = collection_Book.find(query6);
                    while(cursor6.hasNext()){
                        System.out.println(cursor6.next());
                    }
                    System.out.println();
                    break;
                case "7":   //Update a book information
                    BasicDBObject newStreet = new BasicDBObject().append("$set", new BasicDBObject().append("publisher.$[].address.$[].street", "CS157C NoSQL Highway North"  ));
                    collection_Book.update(new BasicDBObject().append("title", "MongoDB: The Definitive Guide: Powerful and Scalable Data Storage"), newStreet);
                    break;
                default:
                    break;
            }
        }while (!selection.equals("8"));
        mongo.close();


    }

    /**
     * Initialize the running environment.
     */
    private static void initialize() throws UnknownHostException {
        db.dropDatabase();
        db = mongo.getDB("book_store");
        collection_Author = db.getCollection("author");
        String json_Author ="{ " +
                "'firstname': 'Eoin'," +
                "'lastname': 'Brazil'," +
                "'country': 'Ireland'," +
                "'books': ['MongoDB: The Definitive Guide: Powerful and Scalable Data Storage']"+
                "}";
        DBObject dbObject =(DBObject) JSON.parse(json_Author);
        collection_Author.insert(dbObject);

        String json_Author2 ="{ " +
                "'firstname': 'Shannon'," +
                "'lastname': 'Bradshaw'," +
                "'country': ''," +
                "'books': ['MongoDB: The Definitive Guide: Powerful and Scalable Data Storage']"+
                "}";
        DBObject dbObject2 =(DBObject) JSON.parse(json_Author2);
        collection_Author.insert(dbObject2);

        String json_Author3 ="{ " +
                "'firstname': 'Kristina'," +
                "'lastname': 'Chodorow'," +
                "'country': 'USA'," +
                "'books': ['MongoDB: The Definitive Guide: Powerful and Scalable Data Storage', " +
                "'Scaling MongoDB: Sharding, Cluster Setup, and Administration', " +
                "'50 Tips and Tricks for MongoDB Developers: Get the Most Out of Your Database']"+
                 "}";
        DBObject dbObject3 =(DBObject) JSON.parse(json_Author3);
        collection_Author.insert(dbObject3);

        String json_Author4 ="{ " +
                "'firstname': 'Jeff'," +
                "'lastname': 'Carpenter'," +
                "'country': 'USA'," +
                "'books': ['Cassandra: The Definitive Guide: Distributed Data at Web Scale']"+
                "}";
        DBObject dbObject4 =(DBObject) JSON.parse(json_Author4);
        collection_Author.insert(dbObject4);

        String json_Author5 ="{ " +
                "'firstname': 'Eben'," +
                "'lastname': 'Hewitt'," +
                "'country': 'USA'," +
                "'books': ['Cassandra: The Definitive Guide: Distributed Data at Web Scale', 'Java Garage', " +
                    "'Technology Strategy Patterns', 'Semantic Software Design', 'Java SOA Cookbook']"+
                "}";
        DBObject dbObject5 =(DBObject) JSON.parse(json_Author5);
        collection_Author.insert(dbObject5);

        //--------------------------------------------------00000-------------------------

        collection_Customer = db.getCollection("customer");
        String json_customer ="{ " +
                "'username': 'Lifan Zeng'," +
                "'password': '123456'," +
                "'active': true," +
                "'address': [{'street': '1234 Happy Live Street', 'city': 'Modesto', 'zip': '95355', 'state': 'Ca', 'country': 'USA'}],"+
                "'date_of_creation':'2016-01-08',"+
                "'orders': [{'books': [{'title':'MongoDB: The Definitive Guide: Powerful and Scalable Data Storage', 'ISBN': '978-1491954461'}], 'order_date': '2020-05-12', 'delivery_date': '2020-05-15'}]"+              //order 类型？
                "}";
        DBObject dbObject_cu =(DBObject) JSON.parse(json_customer);
        collection_Customer.insert(dbObject_cu);

        String json_customer2 ="{ " +
                "'username': 'Jack Chen'," +
                "'password': '222222'," +
                "'active': true," +
                "'address': [{'street': '2222 Century Blvd', 'city': 'Los Angle', 'zip': 90304, 'state': 'CA', 'country': 'USA'}],"+
                "'date_of_creation':'2018-10-21',"+
                "'orders': [{'books': [{'title':'MongoDB: The Definitive Guide: Powerful and Scalable Data Storage', 'ISBN': '978-1491954461'}, {'title': 'Java Garage', 'ISBN': '978-0321246233' }], 'order_date': '2020-06-13', 'delivery_date': '2020-06-17'}, " +
                "{'books': [{'title':'Cassandra: The Definitive Guide: Distributed Data at Web Scale', 'ISBN': '978-1098115166'}], 'order_date': '2020-07-02', 'delivery_date': '2020-07-03'}]}";
        DBObject dbObject_cu2 =(DBObject) JSON.parse(json_customer2);
        collection_Customer.insert(dbObject_cu2);

        String json_customer3 ="{ " +
                "'username': 'Mike Tseng'," +
                "'password': '333333'," +
                "'active': true," +
                "'address': [{'street': '3333 Century Blvd', 'city': 'San Jose', 'zip': 95112, 'state': 'CA', 'country': 'USA'}],"+
                "'date_of_creation':'2017-10-15',"+
                "'orders': [{'books': [{'title':'Cassandra: The Definitive Guide: Distributed Data at Web Scale', 'ISBN': '978-1098115166'}, {'title': 'Java Garage', 'ISBN': '978-0321246233' }], 'order_date': '2019-05-18', 'delivery_date': '2019-05-18'}]"+              //order 类型？
                "}";
        DBObject dbObject_cu3 =(DBObject) JSON.parse(json_customer3);
        collection_Customer.insert(dbObject_cu3);

        String json_customer4 ="{ " +
                "'username': 'Ming Lam'," +
                "'password': '444444'," +
                "'active': true," +
                "'address': [{'street': '4444 Century Blvd', 'city': 'San Jose', 'zip': 95112, 'state': 'CA', 'country': 'USA'}],"+
                "'date_of_creation':'2012-10-15',"+
                "'orders': [{'books': [{'title':'Cassandra: The Definitive Guide: Distributed Data at Web Scale', 'ISBN': '978-1098115166'}], 'order_date': '2020-05-08', 'delivery_date': '2020-05-18'}]"+              //order 类型？
                "}";
        DBObject dbObject_cu4 =(DBObject) JSON.parse(json_customer4);
        collection_Customer.insert(dbObject_cu4);

        String json_customer5 ="{ " +
                "'username': 'Richard Dong'," +
                "'password': '555555'," +
                "'active': true," +
                "'address': [{'street': '55555 Century Blvd', 'city': 'San Francisco', 'zip': 94122, 'state': 'CA', 'country': 'USA'}],"+
                "'date_of_creation':'2014-10-15',"+
                "'orders': [{'books': [{'title':'50 Tips and Tricks for MongoDB Developers: Get the Most Out of Your Database', 'ISBN': '978-1449304614'}], 'order_date': '2020-06-03', 'delivery_date': '2020-06-06'}]"+              //order 类型？
                "}";
        DBObject dbObject_cu5 =(DBObject) JSON.parse(json_customer5);
        collection_Customer.insert(dbObject_cu5);

        //----------------------------------------------------------------------

        collection_Book = db.getCollection("book");
        String json_Book ="{ " +
                "'title': 'MongoDB: The Definitive Guide: Powerful and Scalable Data Storage'," +
                "'authors': ['Shannon Bradshaw', 'Eoin Brazil', 'Kristina Chodorow']," +
                "'ISBN': '978-1491954461'," +
                "'publisher': [{'name': 'OReilly Media', 'date': '2019-12-31', 'city': 'Sebastopol', 'address': " +
                    "[{'street': '1005 Gravenstein Highway North', 'city': 'Sebastopol', 'zip': 95472, 'state': 'CA', 'country': 'USA'}]}],"+
                "'available': true,"+
                "'pages': 514,"+

                "'summary': 'In six parts, this book shows you how to: " +
                "Work with MongoDB, perform write operations, find documents, and create complex queries" +
                "Index collections, aggregate data, and use transactions for your application" +
                "Configure a local replica set and learn how replication interacts with your application" +
                "Set up cluster components and choose a shard key for a variety of applications" +
                "Explore aspects of application administration and configure authentication and authorization" +
                "Use stats when monitoring, back up and restore deployments, and use system settings when deploying MongoDB',"+

                "'reviews': {'ratingNumber': 104, 'grades out of 5': 4.6, 'review_body':[{'customer': 'L. V. Smart', 'stars': 5, 'review': 'An excellent book " +
                "on MongoDB. Well thought out with good examples that I could use IRL. I like how the book is partitioned - setting" +
                " up MongoDB and then indexing, configuring, setting up clustering, admin and statistics. It flows very well.'}, " +
                "{'customer': 'Mary Chen', 'stars': 4, 'review': 'This is a good MongoDB book.'}]},"+
                "'language': 'English',"+
                "'categories': ['Data Mining', 'Database Storage & Design', 'Data Processing']"+
                "}";
        DBObject dbObject_bo =(DBObject) JSON.parse(json_Book);
        collection_Book.insert(dbObject_bo);

        String json_Book2 ="{ " +
                "'title': 'Scaling MongoDB: Sharding, Cluster Setup, and Administration'," +
                "'authors': ['Kristina Chodorow']," +
                "'ISBN': '978-1449303211'," +
                "'publisher': [{'name': 'OReilly Media', 'date': '2019-12-31', 'city': '', 'address': " +
                    "[{'street': '1005 Gravenstein Highway North', 'city': 'Sebastopol', 'zip': 95472, 'state': 'CA', 'country': 'USA'}]}],"+
                "'available': true,"+
                "'pages': 79,"+

                "'summary': 'Scaling MongoDB will help you:" +
                "Set up a MongoDB cluster through sharding" +
                "Work with a cluster to query and update data" +
                "Operate, monitor, and backup your cluster" +
                "Plan your application to deal with outages',"+

                "'reviews': {'ratingNumber': 10, 'grades out of 5': 3.6, 'review_body': [{'customer': 'Tyler', 'stars': 4, 'review': 'Great information, and very useful. One complaint: it assumed too much" +
                " from the reader. On the one hand, you are reading it because you are ready for scale, meaning you have traveled the Mongo road; on the other hand, even then" +
                " it was quite a difficult read. Eg, I never sussed out the difference between replica sets, shards, and clusters. Maybe an intro on what would lead someone to" +
                " this point of scale (how much data is too much on a single instance), what specifically are the benefits sharding will provide, show some very basic sample" +
                " app-code (I vote Node.js). But anyway, while it may require reading twice and picking up the missing pieces elsewhere, it was still very useful information.'}," +
                "{'customer': 'Roberto', 'stars': 5, 'review': 'he fact that the modern nosql databases were made for horizontal scalability does not mean at all we should not design" +
                " our schemas to take advantage of theirs scalability model. This book gives valuable advice for schema and cluster design and administration and it is a must read if you" +
                " are doing any real work with MongoDB.'}]},"+

                "'language': 'English',"+
                "'categories': ['Data Warehousing', 'Data Mining', 'Data Modeling & Design']"+
                "}";
        DBObject dbObject_bo2 =(DBObject) JSON.parse(json_Book2);
        collection_Book.insert(dbObject_bo2);

        String json_Book3 ="{ " +
                "'title': '50 Tips and Tricks for MongoDB Developers: Get the Most Out of Your Database'," +
                "'authors': ['Kristina Chodorow']," +
                "'ISBN': '978-1449304614'," +
                "'publisher': [{'name': 'OReilly Media', 'date': '2019-12-31', 'city': '', 'address': " +
                    "[{'street': '1005 Gravenstein Highway North', 'city': 'Sebastopol', 'zip': 95472, 'state': 'CA', 'country': 'USA'}]}],"+
                "'available': true ,"+
                "'pages': 66,"+

                "'summary': 'You get specific guidance in five topic areas directly from engineers at 10gen, the company that develops and supports this open source database:" +
                "Application Design Tips: What to keep in mind when designing your schema" +
                "Implementation Tips: Programming applications against MongoDB" +
                "Optimization Tips: Speeding up your applications" +
                "Data Safety Tips: Using replication and journaling to keep data safe—without sacrificing too much performance" +
                "Administration Tips: How to configure MongoDB and keep it running smoothly',"+

                "'reviews': {'ratingNumber': 13, 'grades out of 5': 3.1, 'review_body': [{'customer': 'Kelly H', 'stars': 1, 'review': 'This should have been included in a larger MongoDB book, and most of the information probably is. For the price it is a complete waste. Basically just a pamphlet. Do not waste your time.'}, " +
                "{'customer': 'Pedro Otoya', 'stars': 5, 'review': 'Great book for a even better product\n" +
                "It covers every single aspect you have to take into account when building an optimize application using a great nosql database'}" +
                "]},"+
                "'language': 'English',"+
                "'categories': ['Data Warehousing', 'Data Mining', 'Data Modeling & Design']"+
                "}";
        DBObject dbObject_bo3 =(DBObject) JSON.parse(json_Book3);
        collection_Book.insert(dbObject_bo3);

        String json_Book4 ="{ " +
                "'title': 'Cassandra: The Definitive Guide: Distributed Data at Web Scale'," +
                "'authors': ['Jeff Carpenter', 'Eben Hewitt']," +
                "'ISBN': '978-1098115166'," +
                "'publisher': [{'name': 'OReilly Media', 'date': '2019-12-31', 'city': '', 'address': " +
                    "[{'street': '1005 Gravenstein Highway North', 'city': 'Sebastopol', 'zip': 95472, 'state': 'CA', 'country': 'USA'}]}],"+
                "'available': true,"+
                "'pages': 428,"+

                "'summary': 'This guide helps you harness Cassandra’s speed and flexibility. " +
                "Understand Cassandra’s distributed and decentralized structure. " +
                "Use the Cassandra Query Language (CQL) and cqlsh—the CQL shell. " +
                "Create a working data model and compare it with an equivalent relational model. " +
                "Develop sample applications using client drivers for languages including Java, Python, and Node.js. " +
                "Explore cluster topology and learn how nodes exchange data.',"+

                "'reviews': {'ratingNumber': 17, 'grades out of 5': 4.2, 'review_body': [{'customer': 'Brick Mover', 'stars': 4, 'review': 'I especially enjoy reading the chapter on data modeling.'}, " +
                "{'customer': 'Krishnan', 'stars': 5, 'review': 'This is the most comprehensive book to learn about Cassandra, the best book without doubt, useful examples and relevant. " +
                "Just go for it if you are new to Cassandra, without much consideration.'}" +
                "]},"+

                "'language': 'English',"+
                "'categories': ['Database Management Systems', 'Distributed Systems & Computing', 'Desktop Database Books']"+
                "}";
        DBObject dbObject_bo4 =(DBObject) JSON.parse(json_Book4);
        collection_Book.insert(dbObject_bo4);

        String json_Book5 ="{ " +
                "'title': 'Java Garage'," +
                "'authors': ['Eben Hewitt']," +
                "'ISBN': '978-0321246233'," +
                "'publisher': [{'name': 'Prentice Hall Ptr', 'date': '2004-08-09', 'city': 'Hoboken', 'address': [{'street': '', 'city': 'Hoboken', 'zip': '', 'state': 'New Jersey', 'country': 'USA'}]}],"+
                "'available': true,"+
                "'pages': 512,"+
                "'summary': 'Adopting an unconventional style bordering on the sarcastic, Hewitt offers his personal take on operators, strings, arrays, inheritance, " +
                    "declaring and modifying classes, documenting code with Javadoc, handling exceptions, creating GUIs with Swing, and using the system and runtime classes." +
                    " \"Garage\" in the title refers to the idea of the workshop--the place where you go to tinker around with stuff, to tear it apart and build it back together again," +
                    " to repair something that is not working.',"+
                "'reviews': {'ratingNumber': 8, 'grades out of 5': 3.7, 'review_body': [{'customer': 'A. Walsh', 'stars': 4, " +
                    "'review': 'I have been writing java code on and off for years, but never studied it formally. I bought this book to flesh out my knowledge and fill in the spaces, " +
                    "which it did quite well. If you have time and inclination for a thousand page book like OReilly s \"Learning Java,\" great. If you want a quick refresher or " +
                    "intro - this book is perfect.'}, " +
                    "{'customer': 'Thomas Paul', 'stars': 2, 'review': 'Headache. That is what I got every time I picked up this book. Too cute. Too many short sentences. " +
                    "Sentence fragments. Headache. Recipes. Like reading my 12-year-old daughter s instant messages.'}]},"+
                "'language': 'English',"+
                "'categories': ['Java Programming', 'Computer Programming Languages', 'Computer Software']"+
                "}";
        DBObject dbObject_bo5 =(DBObject) JSON.parse(json_Book5);
        collection_Book.insert(dbObject_bo5);

        //-----------------------------------------------------------

        collection_Order = db.getCollection("order");
        String json_Order ="{ " +
                "'book': [{'title':'MongoDB: The Definitive Guide: Powerful and Scalable Data Storage', 'ISBN': '978-1491954461'}, {'title': 'Java Garage', 'ISBN': '978-0321246233' }]," +
                "'order_date': '2020-05-12',"+
                "'delivery_date': '2020-05-15'"+
                "}";
        DBObject dbObject_ord =(DBObject) JSON.parse(json_Order);
        collection_Order.insert(dbObject_ord);

        String json_Order2 ="{ " +
                "'book': [{'title':'MongoDB: The Definitive Guide: Powerful and Scalable Data Storage', 'ISBN': '978-1491954461'}]," +
                "'order_date': '2020-06-13',"+
                "'delivery_date': '2020-06-17'"+
                "}";
        DBObject dbObject_ord2 =(DBObject) JSON.parse(json_Order2);
        collection_Order.insert(dbObject_ord2);

        String json_Order3 ="{ " +
                "'book': [{'title':'Cassandra: The Definitive Guide: Distributed Data at Web Scale', 'ISBN': '978-1098115166'}]," +
                "'order_date': '2020-07-02',"+
                "'delivery_date': '2020-07-03'"+
                "}";
        DBObject dbObject_ord3 =(DBObject) JSON.parse(json_Order3);
        collection_Order.insert(dbObject_ord3);

        String json_Order4 ="{ " +
                "'book': [{'title':'Cassandra: The Definitive Guide: Distributed Data at Web Scale', 'ISBN': '978-1098115166'}, {'title': 'Java Garage', 'ISBN': '978-0321246233' }]," +
                "'order_date': '2019-05-18',"+
                "'delivery_date': '2019-05-18'"+
                "}";
        DBObject dbObject_ord4 =(DBObject) JSON.parse(json_Order4);
        collection_Order.insert(dbObject_ord4);

        String json_Order5 ="{ " +
                "'book': [{'title':'Cassandra: The Definitive Guide: Distributed Data at Web Scale', 'ISBN': '978-1098115166'}]," +
                "'order_date': '2020-05-08',"+
                "'delivery_date': '2020-05-18'"+
                "}";
        DBObject dbObject_ord5 =(DBObject) JSON.parse(json_Order5);
        collection_Order.insert(dbObject_ord5);

        String json_Order6 ="{ " +
                "'book': [{'title':'50 Tips and Tricks for MongoDB Developers: Get the Most Out of Your Database', 'ISBN': '978-1449304614'}]," +
                "'order_date': '2020-06-03',"+
                "'delivery_date': '2020-06-06'"+
                "}";
        DBObject dbObject_ord6 =(DBObject) JSON.parse(json_Order6);
        collection_Order.insert(dbObject_ord6);
    }
}
