package legacy;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import audio.core.SongPoint;


public class SQLDatabase {

	private final String location = "jdbc:sqlite:D:\\Audio Recoginition\\Gradle\\android\\assets\\sql.db";
	private Connection c = null;

	public SQLDatabase() {
		try {
			c = DriverManager.getConnection(location);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertIntoSongList(int id, String title) {
		try {
			Statement stmt = c.createStatement();
			String in = "INSERT INTO SONG_LIST (ID,TITLE) " + "VALUES (" + id + ", " + "'" + title + "'" + ");";
			stmt.executeQuery(in);
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("[SQLITE] Inserted Song: " + id + " Title: " + title);
	}

	public void insertSongData(List<SongPoint> songData) {
		try {
			c.setAutoCommit(false);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (SongPoint p : songData) {
			try {
				Statement stmt = c.createStatement();
				String in = "INSERT INTO SONG_POINT (HASH,SID,TIME) " + "VALUES (" + p.hash + ", " + p.songID +", " + p.time + ");";
				stmt.executeQuery(in);
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void deleteAllSongList() {
		try {
			Statement stmt = c.createStatement();
			String in = "DELETE FROM SONG_LIST";
			stmt.executeQuery(in);
			stmt.close();
		} catch (SQLException e) {
		
		} finally {
			System.out.println("[SQLITE] Deleted all songs in SONG_LIST");
		}
	}
	
	public void deleteAllSongPoint() {
		try {
			Statement stmt = c.createStatement();
			String in = "DELETE FROM SONG_POINT";
			stmt.executeQuery(in);
			stmt.close();
		} catch (SQLException e) {
			System.out.println("[SQLITE] no songs to delete in SONG_POINT");
		} finally {
			System.out.println("[SQLITE] Deleted all songs in SONG_POINT");
		}

	}
	
	public void checkTables() {
		
		
		
		try {
			c = DriverManager.getConnection(location);

			Statement stmt;
			
			DatabaseMetaData meta = c.getMetaData();
			ResultSet r = meta.getTables(null, null, "SONG_POINT", null);
			if(r.next()) {
				System.out.println("[SQLITE] SONG_POINT EXIST");
				// table exists
				stmt = c.createStatement();
				String check = "DROP TABLE SONG_POINT;";
				stmt.executeQuery(check);
				stmt.close();
			} else { 
				System.out.println("[SQLITE] SONG_POINT NOT EXIST");
				stmt = c.createStatement();
				String createSongPointTable = "CREATE TABLE SONG_POINT " + "(HASH INT    NOT NULL,"
						+ "SID INT   NOT NULL," + "TIME INT    NOT NULL," + "FOREIGN KEY(SID) REFERENCES SONG_LIST(ID))";
				stmt.executeUpdate(createSongPointTable);
				stmt.close();
			}
			
			ResultSet n = meta.getTables(null, null, "SONG_LIST", null);
			if(n.next()) {
				System.out.println("[SQLITE] SONG_LIST EXISTS");
				// table exists
				stmt = c.createStatement();
				String check = "DROP TABLE IF SONG_LIST;";
				stmt.executeQuery(check);
				stmt.close();
			} else { 
				System.out.println("[SQLITE] SONG_LIST NOT EXIST");
				stmt = c.createStatement();
				String createSongTable = "CREATE TABLE SONG_LIST " + "(ID INT PRIMARY KEY    NOT NULL,"
						+ "TITLE      TEXT     NOT NULL)";
				stmt.executeUpdate(createSongTable);
				stmt.close();
			}
			n.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
