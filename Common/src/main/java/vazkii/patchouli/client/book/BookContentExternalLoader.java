package vazkii.patchouli.client.book;

import com.google.gson.JsonElement;

import net.minecraft.resources.ResourceLocation;

import org.apache.commons.io.FilenameUtils;

import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookFolderLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * BookContentLoader that loads from the .minecraft/patchouli_books folder
 */
public final class BookContentExternalLoader implements BookContentLoader {
	public static final BookContentExternalLoader INSTANCE = new BookContentExternalLoader();

	private BookContentExternalLoader() {}

	@Override
	public void findFiles(Book book, String dir, List<ResourceLocation> list) {
		File root = new File(BookFolderLoader.loadDir, book.id.getPath());
		File enUs = new File(root, BookContentsBuilder.DEFAULT_LANG);
		if (enUs.exists()) {
			File searchDir = new File(enUs, dir);
			if (searchDir.exists()) {
				crawl(searchDir, searchDir, list);
			}
		}
	}

	private void crawl(File realRoot, File root, List<ResourceLocation> list) {
		File[] files = root.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				crawl(realRoot, f, list);
			} else if (f.getName().endsWith(".json")) {
				list.add(relativize(realRoot, f));
			}
		}
	}

	private ResourceLocation relativize(File root, File f) {
		String rootPath = root.getAbsolutePath();
		String filePath = f.getAbsolutePath().substring(rootPath.length() + 1);
		String cleanPath = FilenameUtils.removeExtension(FilenameUtils.separatorsToUnix(filePath));

		return new ResourceLocation(PatchouliAPI.MOD_ID, cleanPath);
	}

	@Override
	public JsonElement loadJson(Book book, ResourceLocation resloc, ResourceLocation fallback) {
		try {
			String path = resloc.getPath().substring(BookFolderLoader.loadDir.getName().length());
			File targetFile = new File(BookFolderLoader.loadDir, path);
			if (targetFile.exists()) {
				return BookContentLoader.streamToJson(new FileInputStream(targetFile));
			}
			if (fallback != null) {
				return loadJson(book, fallback, null);
			}
		} catch (IOException e) {
			PatchouliAPI.LOGGER.warn("Failed to load {}.", resloc, e);
			return null;
		}

		return null;
	}

}
