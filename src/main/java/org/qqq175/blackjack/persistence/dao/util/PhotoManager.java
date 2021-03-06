package org.qqq175.blackjack.persistence.dao.util;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Part;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * Contains methods to find or save user's avatars
 * 
 * @author qqq175
 *
 */
public class PhotoManager {

	public enum Result {
		OK("OK"), WRONG_FILETYPE("Wrong file filetype."), IO_ERROR("Unable to save file.");
		private String message;

		private Result(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	/**
	 * find user's photo path by user id
	 * 
	 * @param userId
	 * @return
	 */
	public String findPhotoRelativePath(UserId userId) {
		Settings settings = Settings.getInstance();
		String photoDirPath = settings.getRealPath() + settings.getPhotoFolder() + File.separator;
		String photoFilePath = null;
		for (String extension : settings.getPhotoExtensions()) {
			File photo = new File(photoDirPath + userId.getValue() + extension);
			if (photo.exists()) {
				photoFilePath = settings.getContextPath() + File.separator + settings.getPhotoFolder() + File.separator + userId.getValue()
						+ extension;
			}
		}

		if (photoFilePath == null) {
			photoFilePath = settings.getContextPath() + File.separator + settings.getPhotoFolder() + File.separator + settings.getDefaultPhoto();
		}

		return photoFilePath;
	}

	/**
	 * Upload and save user's avatar
	 * 
	 * @param part
	 * @param userId
	 * @return
	 */
	public Result uploadPhoto(Part part, UserId userId) {
		Result result;

		String fileExtension = getFileExtension(part);

		if (!fileExtension.isEmpty()) {
			String photoFilePath = Settings.getInstance().getRealPath() + Settings.getInstance().getPhotoFolder() + File.separator
					+ userId.getValue();
			deletePhoto(photoFilePath);
			try {
				part.write(photoFilePath + fileExtension);
				result = Result.OK;
			} catch (IOException e) {
				result = Result.IO_ERROR;
			}
		} else {
			result = Result.WRONG_FILETYPE;
		}

		return result;
	}

	/**
	 * delete user's photo
	 * 
	 * @param photoFilePath
	 */
	private void deletePhoto(String photoFilePath) {
		for (String extension : Settings.getInstance().getPhotoExtensions()) {
			File photo = new File(photoFilePath + extension);
			if (photo.exists()) {
				photo.delete();
			}
		}
	}

	/**
	 * Extract uploaded file extension and return it if supported? else return
	 * an empty string
	 * 
	 * @param part
	 * @return
	 */
	private String getFileExtension(Part part) {
		final String HEADER = "content-disposition";
		final String PARAMETER = "filename";
		final String PARAMS_DELIMETER = ";";
		final String VALUE_DELIMETER = ";";
		final String QUOTE = "\"";

		String partHeader = part.getHeader(HEADER);
		String fileName = "";

		// extract file name
		for (String content : partHeader.split(PARAMS_DELIMETER)) {
			if (content.trim().startsWith(PARAMETER)) {
				fileName = content.substring(content.indexOf(VALUE_DELIMETER) + 1).trim().replace(QUOTE, "");
			}
		}
		// try to find file supported extension
		Pattern pattern = Pattern.compile(StringConstant.PATTERN_PHOTO_EXTENSION);
		Matcher match = pattern.matcher(fileName.toLowerCase());
		if (match.find()) {
			return match.group();
		} else {
			return "";
		}
	}
}
