package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.BoardDTO;
import util.DBUtil;

public class BoardDAO {

	public int insertBoard(BoardDTO board) {
		String sql = "INSERT INTO board(title, content, writer_id, category, writer_nickname) VALUES (?, ?, ?, ?, ?)";
		
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getContent());
			pstmt.setString(3, board.getWriterId());
			pstmt.setString(4, board.getCategory());
			pstmt.setString(5, board.getWriterNickname());
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public List<BoardDTO> getBoardList() {
		List<BoardDTO> list = new ArrayList<>();
		String sql = """
			    SELECT board_id, title, writer_id, category, created_at, updated_at, view_count, writer_nickname
			    FROM board
			    ORDER BY 
			        CASE 
			            WHEN category = '공지사항' THEN 0 
			            ELSE 1 
			        END,
			        board_id DESC
			""";
		
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			 ResultSet rs = pstmt.executeQuery()) {
			
			while (rs.next()) {
				BoardDTO board = new BoardDTO();
				board.setBoardId(rs.getInt("board_id"));
				board.setTitle(rs.getString("title"));
				board.setWriterId(rs.getString("writer_id"));
				board.setCategory(rs.getString("category"));
				board.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
				board.setViewCount(rs.getInt("view_count"));
				board.setWriterNickname(rs.getString("writer_nickname"));
				list.add(board);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public BoardDTO getBoardById(int boardId) {
		String sql = "SELECT board_id, title, content, writer_id, category, created_at, view_count, writer_nickname FROM board WHERE board_id = ?";
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setInt(1, boardId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					BoardDTO board = new BoardDTO();
					board.setBoardId(rs.getInt("board_id"));
					board.setTitle(rs.getString("title"));
					board.setContent(rs.getString("content"));
					board.setWriterId(rs.getString("writer_id"));
					board.setCategory(rs.getString("category"));
					board.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
					board.setViewCount(rs.getInt("view_count"));
					board.setWriterNickname(rs.getString("writer_nickname"));
					return board;
				}
				
			} catch (Exception e) {
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void increaseViewCount(int boardId) {
		String sql = "UPDATE board SET view_count = view_count + 1 WHERE board_id = ?";
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			 
			pstmt.setInt(1, boardId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int updateBoard(BoardDTO board) {
		String sql = "UPDATE board SET title = ?, content = ?, category = ?, updated_at = NOW() WHERE board_id = ?";
		
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getContent());
			pstmt.setString(3, board.getCategory());
			pstmt.setInt(4, board.getBoardId());
			
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public int deleteBoard(int boardId) {
		String sql = "DELETE FROM board WHERE board_id = ?";
		
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setInt(1, boardId);
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public List<BoardDTO> getBoardListByCategory(String category) {
		List<BoardDTO> list = new ArrayList<>();
		String sql = """
				SELECT board_id, title, writer_id, category, created_at, updated_at, view_count, writer_nickname
				FROM board
				WHERE category = ?
				ORDER BY 
					CASE WHEN category = '공지사항' THEN 0 ELSE 1 END,
					board_id DESC
			""";
		
		try (Connection conn = DBUtil.getConnection();
				 PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, category);
				try (ResultSet rs = pstmt.executeQuery()) {
					while (rs.next()) {
						BoardDTO board = new BoardDTO();
						board.setBoardId(rs.getInt("board_id"));
						board.setTitle(rs.getString("title"));
						board.setWriterId(rs.getString("writer_id"));
						board.setCategory(rs.getString("category"));
						board.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
						board.setViewCount(rs.getInt("view_count"));
						board.setWriterNickname(rs.getString("writer_nickname"));
						list.add(board);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	public List<BoardDTO> searchBoard(String type, String keyword, String category) {
		List<BoardDTO> list = new ArrayList<>();
		String sql = "SELECT board_id, title, writer_id, category, created_at, view_count, writer_nickname "
				   + "FROM board WHERE 1=1 ";
		
	    if (category != null && !category.equals("전체")) {
	        sql += "AND category = ? ";
	    }
		
		switch (type) {
		case "title": { 
			sql += "AND title LIKE ? ";
			break;
		}
		
		case "content": { 
			sql += "AND content LIKE ? ";
			break;
		}
		
		case "writer": { 
			sql += "AND writer_nickname LIKE ? ";
			break;
		}
		default:
			sql += "AND (title LIKE ? OR content LIKE ? OR writer_nickname LIKE ?) ";
			break;
		}
		sql += "ORDER BY CASE WHEN category = '공지사항' THEN 0 ELSE 1 END, board_id DESC";
		
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			String search = "%" + keyword + "%";
			int idx = 1;
			
	        if (category != null && !category.equals("전체")) {
	            pstmt.setString(idx++, category);
	        }
			
			if (type == null || (!type.equals("title") && !type.equals("content") && !type.equals("writer"))) {
				pstmt.setString(idx++, search);
				pstmt.setString(idx++, search);
				pstmt.setString(idx++, search);
			} else {
				pstmt.setString(idx++, search);
			}
			
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					BoardDTO board = new BoardDTO();
					board.setBoardId(rs.getInt("board_id"));
					board.setTitle(rs.getString("title"));
					board.setWriterId(rs.getString("writer_id"));
					board.setCategory(rs.getString("category"));
					board.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
					board.setViewCount(rs.getInt("view_count"));
					board.setWriterNickname(rs.getString("writer_nickname"));
					list.add(board);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	
}
