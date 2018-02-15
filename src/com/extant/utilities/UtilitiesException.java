package com.extant.utilities;

/** Utilities specific error class. */
public class UtilitiesException extends Exception
  {
  int code;
  public static String Messages[] =
    { "Unknown Utilities Error: "
    , "Unable to initialize Defaults file: "
    , "Invalid Option to getOutputParameters: "
    , "Invalid byte in base-64 code "
    , "Required Parameter is Missing "
    , "File Not Found: "
    , "Internal Error in "
    , "Invalid Parameter: "
    , "Invalid Form for number: "
    , "Unmatched delimiter: "
    , "Not implemented: "
    , "Not a directory: "
    , "Invalid Field: "
    , "No property defined for: "
    , "Syntax error in property: "
    , "Property type mismatch: "
    , "Unable to sort variable-length records with sort key ending = 0"
    , "Missing field "
    , "DOSExec Abnormal Termination: "
    , "Tracer "
    , "No Output File Specified"
    , "Input and Output Files are the same: "
    , "Database is not empty: "
    , "Required parameter is missing: "
    , "Unable to get remote time"
    , "Cannot find root directory"
    , "Local Directory is not empty"
    , "Unexpected Error "
    , "Parse Error "
    , "IO Exception: "
    };

  public static final int UNKNOWN = 0;
  public static final int UNABLE_DEFAULTS = 1;
  public static final int BAD_PARM_GET_OPTION = 2;
  public static final int BAD_BASE64_BYTE = 3;
  public static final int NOFILE = 4;
  public static final int FILENOTFOUND = 5;
  public static final int INTERNAL_ERROR = 6;
  public static final int INVALID_PARAM = 7;
  public static final int INVALID_NUMBER_FORM = 8;
  public static final int UNMATCHED_DELIMITER = 9;
  public static final int NOT_IMPLEMENTED = 10;
  public static final int NOT_DIRECTORY = 11;
  public static final int INVALID_FIELD = 12;
  public static final int NO_PROPERTY = 13;
  public static final int PROPERTY_SYNTAX_ERROR = 14;
  public static final int PROPERTY_TYPE_MISMATCH = 15;
  //public static final int VIOLATES_FIXED_LENGTH_RULE = 16; obsolete
  public static final int MISSING_FIELD = 17;
  public static final int DOSEXEC_ABNORMAL = 18;
  public static final int TRACER = 19;
  public static final int NO_OUTPUT_FILE = 20;
  public static final int I_O_FILES_MATCH = 21;
  public static final int DATABASE_NOT_EMPTY = 22;
  public static final int REQD_PARAM_MISSING = 23;
  public static final int REMOTE_TIME_FAILED = 24;
  public static final int NO_ROOT_DIR = 25;
  public static final int DIR_NOT_EMPTY = 26;
  public static final int UNEXPECTED = 27;
  public static final int PARSE_ERROR = 28;
  public static final int IOEXCEPTION = 29;

  public UtilitiesException(int i)
    {
    this( i, "");
    }

  public UtilitiesException( int i, String message )
    {
    super(((i > Messages.length || i < 0)?Messages[i = UNKNOWN]:
      Messages[i]) + message);
    code = i;
    }

  public int getCode()
    {
    return code;
    }
  }

