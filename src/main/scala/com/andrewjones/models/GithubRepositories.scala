package com.andrewjones.models

/** Represents a Github repository.
  *
  * @param full_name The name of the repo.
  * @param size The size of the repo.
  */
case class GithubRepositories(full_name: String, size: Long)
