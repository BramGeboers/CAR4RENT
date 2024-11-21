import { match } from "assert";
import Link from "next/link";

export const transform = (input: string) => {
  input = replaceUrls(input);
  const linkRegex = /\[([^\]]+)\]\(([^)]+)\)/g;
  let result: (JSX.Element | string)[] = [];
  let lastIndex = 0;
  let match;

  while ((match = linkRegex.exec(input)) !== null) {
    const [fullMatch, text, url] = match;
    const prefix = input.substring(lastIndex, match.index);
    result.push(
      prefix,

      <Link href={url} className="underline text-primary">
        {text}
      </Link>
    );
    lastIndex = linkRegex.lastIndex;
  }

  if (lastIndex < input.length) {
    result.push(input.substring(lastIndex));
  }

  return <>{result}</>;
};

function replaceUrls(text: string): string {
  return text
    .replace(
      /\(?\[?(https?:\/\/)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9@():%_\+.~#?&//=]*)/g,
      (match) => {
        let url = match;
        if (url.startsWith("(") || url.startsWith("[")) {
          return match;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
          url = "http://" + url;
        }
        console.log(url);
        return `[${match}](${url})`;
      }
    )
    .replace(
      /\(?\[?(https?:\/\/)?localhost\b([-a-zA-Z0-9@():%_\+.~#?&//=]*)/g,
      (match) => {
        let url = match;
        if (url.startsWith("(") || url.startsWith("[")) {
          return match;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
          url = "http://" + url;
        }
        return `[${match}](${url})`;
      }
    );
}

export const transformPreview = (input: string) => {
  // Regular expression to match [text](link)
  const linkRegex = /\[([^\]]+)\]\(([^)]+)\)/g;

  // Replace all occurrences of [text](link) with just the text
  const result = input.replace(linkRegex, (_, text, __) => text);

  return result;
};
