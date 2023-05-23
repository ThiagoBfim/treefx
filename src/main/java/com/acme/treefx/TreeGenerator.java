/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */
package com.acme.treefx;

import javafx.scene.Group;

import java.util.Collections;

import java.util.Arrays;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class TreeGenerator {

  public static final int FLOWERS_NUMBER = 100;
  public int flowersNumber = FLOWERS_NUMBER;
  public Group content;
  public int treeDepth;

  public TreeGenerator(Group content, int treeDepth) {
    this.content = content;
    this.treeDepth = treeDepth;
  }

  public Tree generateTree() {


             try {
                 System.out.println(listaCertificadoDisponiveis());
             } catch (Exception e) {
                 System.out.println(e.getMessage());
                 e.printStackTrace();
             }

    final Tree tree = new Tree(treeDepth);
    Util.addChildToParent(content, tree);

    final Branch root = new Branch();
    Util.addChildToParent(tree, root);
    tree.generations.get(0).add(root); //root branch

    for (int i = 1; i < treeDepth; i++) {
      for (Branch parentBranch : tree.generations.get(i - 1)) {
        final List<Branch> newBranches = generateBranches(parentBranch, i);
        if (newBranches.isEmpty()) {
          tree.crown.add(parentBranch);
        }
        tree.generations.get(i).addAll(generateBranches(parentBranch, i));
      }
    }
    tree.crown.addAll(tree.generations.get(treeDepth - 1));
    tree.leafage.addAll(generateLeafage(tree.crown));
    tree.flowers.addAll(generateFlowers(tree.crown));
    return tree;
  }

  private List<Branch> generateBranches(Branch parentBranch, int depth) {
    List<Branch> branches = new ArrayList<>();
    if (parentBranch == null) { // add root branch
      branches.add(new Branch());
    } else {
      if (parentBranch.length < 10) {
        return Collections.emptyList();
      }
      branches.add(new Branch(parentBranch, Branch.Type.LEFT, depth)); //add side left branch
      branches.add(new Branch(parentBranch, Branch.Type.RIGHT, depth)); // add side right branch
      branches.add(new Branch(parentBranch, Branch.Type.TOP, depth)); //add top branch
    }

    return branches;
  }

  private List<Leaf> generateLeafage(List<Branch> crown) {
    List<Leaf> leafage = new ArrayList<>();
    for (final Branch branch : crown) {
      Leaf leaf = new Leaf(branch);
      leafage.add(leaf);
      Util.addChildToParent(branch, leaf);
    }
    return leafage;
  }

  private List<Flower> generateFlowers(List<Branch> crown) {
    List<Flower> flowers = new ArrayList<>(flowersNumber);
    for (int i = 0; i < flowersNumber; i++) {
      Branch branch = crown.get(RandomUtil.getRandomIndex(0, crown.size() - 1));
      final Flower flower = new Flower(branch);
      Util.addChildToParent(branch, flower);
      flowers.add(flower);
    }
    return flowers;
  }




 private static KeyStore keyStore;

 /**
  * Recupera a lista de certificados digitais instalados no Windows,
  * com as informa��es do certificado, ver a classe Certificado.
  *
  * @return
  * @throws KeyStoreException
  * @throws NoSuchProviderException
  * @throws NoSuchAlgorithmException
  * @throws CertificateException
  * @throws IOException
  */
 public static List<String> listaCertificadoDisponiveis() throws KeyStoreException,
         NoSuchProviderException, NoSuchAlgorithmException, CertificateException, IOException {

     List<String> certificates = new ArrayList<>();

     Enumeration<String> al = getKeyStore().aliases();
     while (al.hasMoreElements()) {
         String alias = al.nextElement();
         if (getKeyStore().containsAlias(alias)) {
             X509Certificate cert = (X509Certificate) getKeyStore().getCertificate(alias);
             if (cert != null) {
                 System.out.println("======>>>");
                 System.out.println(extractDN(cert.getSubjectDN()));
                 System.out.println(alias);
                 System.out.println(cert.getNotBefore());
                 System.out.println(cert.getNotAfter());
                 certificates.add(alias);
             }
         }
     }
     return certificates;

 }

 /**
  * Extrai o Common Name (CN) do certificado. O Common Name do certificado
  * � utilizado para exibir o nome do propriet�rio do certificado, ou seja,
  * para qual pessoa ou empresa o certificado foi emitido.
  *
  * @param subjectDN
  * @return
  */
 private static String extractDN(Principal subjectDN) {
     if (subjectDN != null) {
         String dn = subjectDN.toString();
         return dn;
     }
     return null;
 }

 /**
  * Cria o KeyStore de acesso ao Repositorio de Certificados do
  * Windows.
  *
  * @return
  * @throws KeyStoreException
  * @throws NoSuchProviderException
  * @throws NoSuchAlgorithmException
  * @throws CertificateException
  * @throws IOException
  */
 public static KeyStore getKeyStore() throws KeyStoreException, NoSuchProviderException,
         NoSuchAlgorithmException, CertificateException, IOException {
     if (keyStore == null) {
         String os = System.getProperty("os.name").toLowerCase();
         if (os.contains("win")) {
             keyStore = KeyStore.getInstance("Windows-MY");
         } else if (os.contains("mac")) {
             keyStore = KeyStore.getInstance("KeychainStore");
         } else {
             keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
         }
         keyStore.load(null, null);

     }

     return keyStore;
 }
}
