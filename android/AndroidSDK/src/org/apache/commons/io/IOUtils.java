package org.apache.commons.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;

public class IOUtils
{

	 public static void closeQuietly(InputStream input)
	  {
	    try
	    {
	      if (input != null)
	        input.close();
	    }
	    catch (IOException ioe)
	    {
	    }
	  }
	 
  public static byte[] toByteArray(InputStream input)
    throws IOException
  {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    copy(input, output);
    return output.toByteArray();
  }



  public static int copy(InputStream input, OutputStream output)
    throws IOException
  {
    long count = copyLarge(input, output);
    if (count > 2147483647L) {
      return -1;
    }
    return (int)count;
  }

  public static long copyLarge(InputStream input, OutputStream output)
    throws IOException
  {
    byte[] buffer = new byte[4096];
    long count = 0L;
    int n = 0;
    while (-1 != (n = input.read(buffer))) {
      output.write(buffer, 0, n);
      count += n;
    }
    return count;
  }

  
}
