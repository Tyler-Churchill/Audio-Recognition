package audio.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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
	
	
	public void createTables() {
		/** c = DriverManager.getConnection(location);
		System.out.println("Opened connection to SQLDatabase successfully");
		stmt = c.createStatement();
		String createSongTable = "CREATE TABLE SONG_LIST " + "(ID INT PRIMARY KEY    NOT NULL," + "TITLE      TEXT     NOT NULL)";
		stmt.executeUpdate(createSongTable);
		stmt.close();

		stmt = c.createStatement();
		String createSongPointTable = "CREATE TABLE SONG_POINT " + "(HASH INT PRIMARY KEY    NOT NULL," + "SID INT   NOT NULL," + "TIME INT    NOT NULL," + "FOREIGN KEY(SID) REFERENCES SONG_LIST(ID))";
		stmt.executeUpdate(createSongPointTable);
		stmt.close();
		c.close(); **/
	}
	
	
}
