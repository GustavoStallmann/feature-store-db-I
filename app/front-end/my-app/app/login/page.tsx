import { getSearchResults } from '../../lib/search'

 
export default async function LoginPage({
  searchParams,
}: {
  searchParams: Promise<{ [key: string]: string | string[] | undefined }>
}) {
  const results = await getSearchResults((await searchParams).cpf)
 
  return <div>...</div>
}
