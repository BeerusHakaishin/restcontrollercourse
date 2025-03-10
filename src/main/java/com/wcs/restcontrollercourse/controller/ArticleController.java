package com.wcs.restcontrollercourse.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.wcs.restcontrollercourse.dto.ArticleDto;
import com.wcs.restcontrollercourse.model.Article;
import com.wcs.restcontrollercourse.repository.ArticleRepository;

@RestController
@RequestMapping("/articles")
public class ArticleController {

	@Autowired
	ArticleRepository articleRepository;

	// Get all articles
	//// http://localhost:8080/articles
	@GetMapping
	public List<Article> getArticles() {
		return articleRepository.findAll();
	}

	// Get one article
	//// http://localhost:8080/articles/{id}
	@GetMapping("/{id}")
	public Article getArticle(@PathVariable(required = true) Long id) {
		Optional<Article> optArticle = articleRepository.findById(id);
		// Si mon objet optionnel contient bien un article ,je le renvoie
		if (optArticle.isPresent()) {
			return optArticle.get();
		}
		// Si mon objet n'est pas présent je lance une exception
		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}

	// Create article
	//// http://localhost:8080/articles
	@PostMapping
	public Article createArticle(@Valid @RequestBody ArticleDto articleDto) {
		// On crée l'entité
		Article article = new Article();
		article.setTitle(articleDto.getTitle());
		article.setContent(articleDto.getContent());
		article.setCreation(new Date());
		// On enregistre l'entité dans la Base de Données
		return articleRepository.save(article);
	}

	// Update article
	//// http://localhost:8080/articles/{id}
	@PutMapping("/{id}")
	public Article updateArticle(@Valid @RequestBody ArticleDto articleDto, @PathVariable(required = true) Long id) {

		Optional<Article> optArticle = articleRepository.findById(id);
		// Si mon objet optionnel contient pas d'article ,je renvoie une erreur
		if (optArticle.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		// On modifie l'entité
		Article article = optArticle.get();
		article.setTitle(articleDto.getTitle());
		article.setContent(articleDto.getContent());
		// On enregistre l'entité dans la Base de Données
		return articleRepository.save(article);
	}

	// Delete article
	//// http://localhost:8080/articles/{id}
	@DeleteMapping("/{id}")
	public void deleteArticle(@PathVariable(required = true) Long id) {
		Boolean exist = articleRepository.existsById(id);
		// Si mon article n'xiste pas , je renvoie une erreur
		if (!exist) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		// Si il existe je le supprime de la Base de Données
		articleRepository.deleteById(id);
	}

}
