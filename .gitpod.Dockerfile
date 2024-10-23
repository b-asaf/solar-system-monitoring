# .gitpod.Dockerfile
FROM gitpod/workspace-full

# Install custom tools, runtime, etc.
RUN bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh && \
  sdk install java 17.0.8-tem && \
  sdk default java 17.0.8-tem"

# Install PostgreSQL
USER gitpod
RUN curl -fsSL https://www.postgresql.org/media/keys/ACCC4CF8.asc | gpg --dearmor | sudo tee /etc/apt/trusted.gpg.d/postgresql.gpg > /dev/null && \
  echo "deb http://apt.postgresql.org/pub/repos/apt/ focal-pgdg main" | sudo tee /etc/apt/sources.list.d/postgresql.list && \
  sudo apt-get update && \
  sudo apt-get install -y postgresql-client-14