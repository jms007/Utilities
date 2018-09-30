package com.extant.utilities;

import java.io.*;
import java.nio.channels.*;
import java.util.Properties;

// This class replaces the obsolete class useableFile.  It implements all the
// functionality of that class, and more has been added here.

public class UsefulFile
{
	public static final int ALL_WHITE = 1; // readLine trims all whitespace on both ends
	public static final int RIGHT_WHITE = 2; // readLine trims whitespace at end of line
	public static final int EOL = 3; // readLine trims only the end-of-line chars at end
	// EOL is the default. (There is no option to read a line without trim)

	private RandomAccessFile raf = null;
	private String fileName;
	private FileChannel fileChannel;
	private FileLock fileLock;

	public UsefulFile()
	{
		raf = null;
	}

	public UsefulFile(String fileName) throws IOException
	{
		this(fileName, "rw");
	}

	public UsefulFile(String fileName, String mode) throws IOException
	/*
	 * Note: For a RandomAccessFile, mode must be either "r" or "rw". In addition to
	 * these options, we also allow: "w" delete the file if it exists and then open
	 * a new file with "rw". "w+" if the file exists, append to it, else create a
	 * new file "rw+" same as "w+"
	 */
	{
		if (fileName == null)
			throw new IOException("[UsefulFile.init] Supplied File Name is null!");
		boolean append = false;

		if (mode.equals("r") || mode.equals("rw"))
		{
			append = false;
			if (!exists(fileName))
				throw new IOException("File not found: " + fileName);
		} else if (mode.equals("w"))
		{
			// if ( exists( fileName ) ) delete( fileName );
			if (!delete(fileName))
				; // Console.println( fileName + " NOT deleted!" );
			append = false;
			mode = "rw";
		} else if (mode.equals("w+") || mode.equals("rw+"))
		{
			append = true;
			mode = "rw";
		}
		raf = new RandomAccessFile(fileName, mode);
		this.fileName = fileName;
		if (append)
			this.seekEOF();
	}

	public String read(int n) // Reads (up to) n bytes
			throws IOException
	{
		StringBuffer sb = new StringBuffer(n);
		sb.setLength(n);
		for (int i = 0; i < n; ++i)
		{
			byte b;
			try
			{
				b = raf.readByte();
			} catch (EOFException eofx)
			{
				sb.setLength(i);
				return sb.toString();
			}
			sb.setCharAt(i, (char) b);
		}
		return (new String(sb));
	}

	// Reads to newline, then trims whitespace
	public String readLine(int trimOpt) throws IOException
	{
		if (trimOpt == ALL_WHITE)
			return readLine();
		else if (trimOpt == RIGHT_WHITE)
			return Strings.trimRight(raf.readLine(), " \r\n\t");
		else if (trimOpt == EOL)
			return raf.readLine();
		else
			return raf.readLine(); // EOL is the default
	}

	// Reads to newline, then trims all whitespace on both ends
	public String readLine() throws IOException
	{
		// Reads bytes until '\n' or '\r' is encountered. The control
		// byte is discarded, and if the '\r' is followed by a '\n',
		// that is discarded also. So you'd better end your lines with
		// one of: '\n' or '\r' or "\r\n" (normal)
		// Note we have expanded on this to trim all whitespace and
		// control characters from both ends of the line.
		return (raf.readLine().trim());
	}

	public boolean print(String s)
	{
		return (write(s));
	}

	public boolean write(String s)
	{
		try
		{
			raf.writeBytes(s);
			return (true);
		} catch (Exception e)
		{
			e.printStackTrace();
			return (false);
		}
	}

	public boolean writeLine(String s)
	{
		return (write(s + "\r\n"));
	}

	public boolean writeBytes(byte[] bytes) throws IOException
	{
		raf.write(bytes);
		return (true);
	}

	public void println(String s)
	{
		writeLine(s);
		s = "";
	}

	public void appendLine(String s)
	{
		writeLine(s);
	}

	public void close()
	{
		try
		{
			if (raf != null)
			{
				raf.close();
				raf = null;
			}
		} catch (IOException iox)
		{
			/* ignore */ }
	}

	public long copyToFile(String toFileName) throws IOException
	{
		// This will overwrite an extant file without warning
		return copyToFile(toFileName, false);
	}

	public long copyToFile(String toFileName, boolean append) throws IOException
	{
		// if the destination file exists
		// if append is true, this file will be appended to the extant file
		// if append is false, this file will overwrite the extant file
		// if the destination file does not exist
		// a new file will be created, and this file will be copied to it

		String mode;

		// if ( new File( toFileName ).exists() && !append ) new File( toFileName
		// ).delete();
		if (append)
			mode = "w+";
		else
			mode = "w";
		UsefulFile toFile = new UsefulFile(toFileName, mode);
		int byteSize = 16384;
		byte[] buffer = new byte[byteSize];
		byte[] lastBuffer;
		int count;
		long totalCount = 0;
		this.rewind();
		while (true)
		{
			count = raf.read(buffer);
			totalCount += count;
			if (count < byteSize)
			{
				if (count > 0)
				{
					lastBuffer = new byte[count];
					for (int i = 0; i < count; ++i)
						lastBuffer[i] = buffer[i];
					toFile.writeBytes(lastBuffer);
				}
				break;
			}
			toFile.writeBytes(buffer);
		}
		toFile.close();
		return totalCount;
	}

	// Overwrites an existing file
	public static void copy(String from, String to) throws IOException
	{
		UsefulFile fromFile = new UsefulFile(from, "r");
		fromFile.copyToFile(to);
		fromFile.close();
	}

	public static void mv(String oldname, String newname) throws IOException, UtilitiesException
	{ // For the UNIX hackers
		rename(oldname, newname);
	}

	public static void rename(String oldname, String newname) throws IOException, UtilitiesException
	{
		// If a file named newname exists, it will be deleted without warning.
		// Note this rename will work for any combination of oldname & newname
		// without restriction to the same directory or even the same device.
		// (It's not clear whether java.io.File.renameTo() is this general.)
		UsefulFile source = new UsefulFile(oldname, "r");
		source.copyToFile(newname, false);
		source.close();
		new File(oldname).delete();
	}

	public String getFileName()
	{
		return fileName;
	}

	public boolean EOF()
	{
		if (raf == null)
			return true;
		try
		{
			return (raf.getFilePointer() >= raf.length());
		} catch (IOException iox)
		{
			return true;
		}
	}

	public boolean rewind() throws IOException
	{
		raf.seek(0L);
		return (true);
	}

	public boolean seek(long p) throws IOException
	{
		raf.seek(p);
		return (true);
	}

	public boolean seekEOF()
	{
		try
		{
			return (seek(raf.length()));
		} catch (Exception e)
		{
			e.printStackTrace();
			return (false);
		}
	}

	public long length() throws IOException
	{
		return (raf.length());
	}

	public long getLength() throws IOException
	{
		return (raf.length());
	}

	public RandomAccessFile getRAF()
	{
		return (raf);
	}

	public boolean delete()
	{
		this.close();
		return delete(fileName);
	}

	// There are many other options for FileLock (see JDC TechTips 9-24-2002)
	// The FileLock methods have not been tested.
	public void lock() throws IOException
	// Applies an exclusive lock on the entire file
	// This call will block until the lock is obtained.
	{
		if (fileChannel == null)
			fileChannel = raf.getChannel();
		fileLock = fileChannel.lock();
	}

	public boolean tryLock() throws IOException
	// Attempts to obtain an exclusive lock on the entire file
	// This call is non-blocking. Returns true if the lock was obtained
	{
		if (fileChannel == null)
			fileChannel = raf.getChannel();
		fileLock = fileChannel.tryLock();
		return (fileLock == null);
	}

	public void unlock() throws IOException
	{
		if (fileLock == null)
			return;
		fileLock.release();
	}

	// A tool for generating a temporary file name, guaranteed to be unique in the
	// given directory. The file name is of the form
	// <dir>\Tnnnn.<extention>, 0 <= n <= 9999
	public static String getTempFileName()
	{
		// If no directory is specified, put the file in the temporary directory
		// C:\Documents and Settings\jms\Local Settings\Temp\
		// C:\DOCUME~1\jms\LOCALS~1\Temp
		Properties props = System.getProperties();
		String dir = props.getProperty("java.io.tmpdir");
		return getTempFileName(dir);
	}

	public static String getTempFileName(String dir)
	{
		return getTempFileName(dir, ".tmp");
	}

	public static String getTempFileName(String dir, String extention)
	{
		String trial;
		java.util.Random random = new java.util.Random(java.lang.System.currentTimeMillis());
		if (!dir.endsWith("\\"))
			dir += "\\";
		if (!extention.startsWith("."))
			extention = "." + extention;
		while (true)
		{
			trial = dir + "T" + random.nextInt(10000) + extention;
			if (!new File(trial).exists())
				return (trial);
		}
	}

	public static long getFileSize(String fileName) throws IOException
	{
		return new File(fileName).length();
	}

	// Some convenience tools
	public static boolean exists(String fileName)
	{
		return (new File(fileName).exists());
	}

	public void skip(long nBytes) throws IOException
	{
		// Assumes current position is beginning of the file
		seek(nBytes);
	}

	public static boolean delete(String fileName)
	{
		return (new File(fileName).delete());
	}

	public static String getDir(String path)
	{
		int p = path.lastIndexOf(File.separatorChar);
		if (p < 0)
			return "";
		return path.substring(0, p + 1);
	}

	public static String getName(String path)
	{
		int p = path.lastIndexOf(File.separatorChar);
		if (p < 0)
			return path;
		return path.substring(p + 1);
	}

}
