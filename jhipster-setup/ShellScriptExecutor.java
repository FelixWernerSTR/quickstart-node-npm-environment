import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShellScriptExecutor {
  
  private Path workingDirectory;
  private String script;
  private String parameter;
  
  /**
   * @param script = cmd/sh file
   * @param workDir
   * @return this
   */
  public static ShellScriptExecutor fromScriptAndWorkingDirectory(String script, String workDir) {
    return new ShellScriptExecutor().script(script).workingDirectory(workDir);
  }
  
  /**
   * @param workingDirectory the workingDirectory to set
   * @return this
   */
  public ShellScriptExecutor workingDirectory(String workingDirectory) {
    this.workingDirectory = Paths.get(workingDirectory);
    return this;
  }
  
  /**
   * @param script the script to set
   * @return this
   */
  public ShellScriptExecutor script(String script) {
    this.script = script;
    return this;
  }
  
  /**
   * @param parameter the parameter to set
   * @return this
   */
  public ShellScriptExecutor parameter(String parameter) {
    this.parameter = parameter;
    return this;
  }
  
  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    if (args == null || args.length < 1) {
      System.out.println("put in sh/cmd-script and/or workingDir!");
      return;
    }
    if (args.length == 1) {
      System.out.println("with exit code! " + new ShellScriptExecutor().workingDirectory(args[0]).chmod());
    } else {
      System.out.println("with exit code! " + new ShellScriptExecutor().script(args[0]).workingDirectory(args[1]).parameter(args[2]).process());
    }   
  }
  
  public int process() throws Exception {
    System.out.println("start executing:" + getScript() + " in workingDirectory: " + workingDirectory);

    chmod();
    
    ProcessBuilder processBuilder = new ProcessBuilder(getScript(), parameter);
    Map<String, String> environment = processBuilder.environment();
    System.out.println("environment: ");
    environment.forEach((key, value) -> System.out.println(key + "=" + value));
    processBuilder.directory(workingDirectory.toFile());
    Process process = processBuilder.start();
    
    {
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.contains("--password")) {
          line = line.substring(0, line.indexOf("--password"));
          System.out.println("{} --password=*********" + line);
        } else {
          System.out.println(line);
        }
      }
    }
    
    { // eventuell auch fehler ausgeben, sonst gehen sie leise unter
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
      String line;
      List<String> errors = new ArrayList<>();
      while ((line = reader.readLine()) != null) {
        errors.add(line);
      }
      for (String l : errors) {
        System.out.println(l);
      }
    }
    System.out.println("executing {} finished! " + getScript());
    return 0;
  }
  
  private int chmod() throws Exception {
    if (System.getenv().get("OSTYPE") != null && System.getenv().get("OSTYPE").toLowerCase().startsWith("linux")) {

      ProcessBuilder processBuilder = new ProcessBuilder("chmod", "-R", "775", workingDirectory.toAbsolutePath().toString());
      
      processBuilder.directory(workingDirectory.toFile());
      Process process = processBuilder.start();
      
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
      System.out.println("executing chmod 775 on {} finished !" + workingDirectory.toAbsolutePath().toString());
      return process.waitFor();      
      
    } else {
      System.out.println("attention obviously executing on windows!");
      return 0;
    }

  }
  
  /**
   * @return the script
   */
  private String getScript() {
    return script;
  }
  
}
