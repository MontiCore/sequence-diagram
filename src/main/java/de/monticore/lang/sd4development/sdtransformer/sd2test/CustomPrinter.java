package de.monticore.lang.sd4development.sdtransformer.sd2test;

import de.monticore.prettyprint.IndentPrinter;

public class CustomPrinter extends IndentPrinter {

  @Override
  protected void doPrint(String s) {
    // get position of "\n"
    int pos = s.indexOf("\n");
    while(pos >= 0) {
      String substring = s.substring(0, pos);

      // Start new line if string exceeds maxlinelength
      if(maxlinelength != -1) {
        if(pos + linebuffer.length() > maxlinelength) {
          handleOptionalBreak();
        }
      }
      // Print up to newline, then a newline and new spacer
      linebuffer.append(substring);

      flushBuffer();
      writtenbuffer.append("\n");

      s = s.substring(pos + 1);
      pos = s.indexOf("\n");
    }

    // Start new line if string exceeds maxlinelength
    if(maxlinelength != -1) {
      if(s.length() + linebuffer.length() > maxlinelength) {
        handleOptionalBreak();
      }
    }

    linebuffer.append(s);
    linebuffer.append(",");
  }

  @Override
  public void flushBuffer() {

    optionalBreakPosition = 0;

    // indent nonempty buffers
    if(linebuffer.length() != 0) {
      writtenbuffer.append(spacer);
      linebuffer.deleteCharAt(linebuffer.length() - 1);
      writtenbuffer.append(linebuffer);
    }
    linebuffer.setLength(0);
  }
}
