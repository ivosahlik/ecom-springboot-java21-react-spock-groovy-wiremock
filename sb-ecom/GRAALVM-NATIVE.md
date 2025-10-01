# GraalVM Native Image Build Guide

This project has been configured to support GraalVM Native Image compilation, allowing you to create native executables of your Spring Boot application.

## Prerequisites

1. **GraalVM Installation**: Install GraalVM with native-image support
   ```bash
   # Using SDKMAN (recommended)
   sdk install java 21.0.2-graal
   sdk use java 21.0.2-graal

   # Install native-image component
   gu install native-image
   ```

2. **Required System Tools**:
   - **Linux**: `gcc`, `glibc-devel`, `zlib-devel`
   - **macOS**: Xcode command line tools (`xcode-select --install`)
   - **Windows**: Microsoft Build Tools for Visual Studio

## Native Build Commands

### 1. Standard Native Build
Build a native executable using Maven:
```bash
mvn clean native:compile -Pnative
```

### 2. Spring Boot Native Build (with Buildpacks)
Build using Spring Boot's native buildpack support:
```bash
mvn spring-boot:build-image -Pnative
```

### 3. Test Native Build
Run tests in native mode:
```bash
mvn test -PnativeTest
```

### 4. Build JAR and Native Executable
```bash
# Build standard JAR
mvn clean package

# Build native executable
mvn native:compile -Pnative -DskipTests
```

## Running the Application

### Regular JAR
```bash
java -jar target/ecom-0.0.1-SNAPSHOT.jar
```

### Native Executable
```bash
# After native compilation
./target/ecom
```

## Configuration Files

The project includes GraalVM native image configuration files in `src/main/resources/META-INF/native-image/`:

- **`reflect-config.json`**: Reflection configuration for JPA entities and other classes
- **`resource-config.json`**: Resource inclusion patterns for properties, templates, etc.
- **`jni-config.json`**: JNI configuration (empty by default)
- **`proxy-config.json`**: Dynamic proxy configuration (empty by default)

## Native Profile Configuration

The native profile (`-Pnative`) includes:

- **GraalVM Native Build Tools**: Handles native compilation
- **Optimized Build Args**: Performance and compatibility settings
- **Resource Inclusion**: Automatic inclusion of Spring Boot resources
- **Spring Boot Native Support**: Buildpack integration

## Build Arguments Explained

The native build includes these optimizations:

- `--no-fallback`: Prevents fallback to JVM mode
- `--install-exit-handlers`: Proper application shutdown
- `--enable-url-protocols=http,https`: Web protocol support
- `--report-unsupported-elements-at-runtime`: Runtime error reporting
- `--allow-incomplete-classpath`: Compatibility with Spring Boot
- `-H:+ReportExceptionStackTraces`: Better error reporting
- `-H:+AddAllCharsets`: Character encoding support
- `-H:IncludeResources=.*\\.properties$`: Include properties files

## Troubleshooting

### Common Issues

1. **Missing Reflection Configuration**
   - Add missing classes to `reflect-config.json`
   - Use `@RegisterForReflection` annotation

2. **Resource Not Found**
   - Update `resource-config.json` with missing resources
   - Check resource paths are correct

3. **Compilation Errors**
   ```bash
   # Enable verbose output
   mvn native:compile -Pnative -Dverbose
   ```

4. **Memory Issues During Build**
   ```bash
   # Increase build memory
   export MAVEN_OPTS="-Xmx4g"
   mvn native:compile -Pnative
   ```

### Performance Tips

1. **Optimize Build Time**
   ```bash
   # Skip tests during native build
   mvn native:compile -Pnative -DskipTests
   ```

2. **Profile-Guided Optimizations (PGO)**
   ```bash
   # Run with profiling first, then rebuild
   mvn native:compile -Pnative -Dspring.profiles.active=pgo
   ```

## Expected Benefits

- **Faster Startup**: ~10-100x faster than JVM
- **Lower Memory**: ~50-80% less memory usage
- **Smaller Size**: Executable includes only used code
- **No Warmup**: Peak performance immediately

## Limitations

- **Build Time**: Native compilation takes longer
- **Reflection**: Requires explicit configuration
- **Dynamic Loading**: Limited runtime class loading
- **Debugging**: Different debugging experience

## Environment Variables

Set these for optimal builds:
```bash
export GRAALVM_HOME=/path/to/graalvm
export JAVA_HOME=$GRAALVM_HOME
export PATH=$GRAALVM_HOME/bin:$PATH
```

## Integration with CI/CD

Example GitHub Actions workflow:
```yaml
- name: Setup GraalVM
  uses: graalvm/setup-graalvm@v1
  with:
    java-version: '21'
    distribution: 'graalvm'
    components: 'native-image'

- name: Build Native Image
  run: mvn native:compile -Pnative -DskipTests
```